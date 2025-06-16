package com.copperleaf.ballast.queue.inputstrategy

import com.copperleaf.ballast.InputFilter
import com.copperleaf.ballast.InputStrategy
import com.copperleaf.ballast.InputStrategyScope
import com.copperleaf.ballast.Queued
import com.copperleaf.ballast.core.ChannelInputStrategy
import com.copperleaf.ballast.core.DefaultGuardian
import com.copperleaf.ballast.queue.DeadLetterQueue
import com.copperleaf.ballast.queue.InputDelay
import com.copperleaf.ballast.queue.InputId
import com.copperleaf.ballast.queue.InputJourney
import com.copperleaf.ballast.queue.InputPriority
import com.copperleaf.ballast.queue.InputTimeout
import com.copperleaf.ballast.queue.QueueDriver
import com.copperleaf.ballast.queue.QueueLock
import com.copperleaf.ballast.queue.QueueStateStore
import com.copperleaf.ballast.queue.QueueThrottle
import com.copperleaf.ballast.queue.RetryStrategy
import com.copperleaf.ballast.queue.impl.delay.FixedInputDelay
import com.copperleaf.ballast.queue.impl.dlq.IgnoreDeadLetterQueue
import com.copperleaf.ballast.queue.impl.driver.InMemoryQueueDriver
import com.copperleaf.ballast.queue.impl.id.ReflectiveInputId
import com.copperleaf.ballast.queue.impl.journey.FixedInputJourney
import com.copperleaf.ballast.queue.impl.lock.InMemoryQueueLock
import com.copperleaf.ballast.queue.impl.priority.FixedInputPriority
import com.copperleaf.ballast.queue.impl.retry.FixedRetryStrategy
import com.copperleaf.ballast.queue.impl.store.InMemoryQueueStateStore
import com.copperleaf.ballast.queue.impl.throttle.ExponentialQueueThrottle
import com.copperleaf.ballast.queue.impl.timeout.FixedInputTimeout
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

/**
 * A normal InputStrategy directly reads from the input queue to handle Inputs. A JobQueue instead uses the channel
 * simply as a buffer to read them and place into a persistent queue. Separately, a job polls the queue to pull items
 * off the queue and process them.
 *
 * Inputs must be serializable, since they are stored persistently. Additionally, Inputs can be given a priority so that
 * the order in which they are processed is not necessarily the order in which they were received.
 */
class StatefulJobQueueInputStrategy<Inputs : Any, Events : Any, State : Any>(
    private val queueName: String = "Default",

    internal val inputSerializer: KSerializer<Inputs>,
    internal val stateSerializer: KSerializer<State>,
    override val defaultState: () -> State,

    private val json: Json = Json.Default,
    private val clock: Clock = Clock.System,
    private val driver: QueueDriver = InMemoryQueueDriver(clock),
    private val stateStore: QueueStateStore = InMemoryQueueStateStore(),
    private val throttle: QueueThrottle = ExponentialQueueThrottle(),
    private val timeout: InputTimeout<Inputs, Events, State> = FixedInputTimeout(),
    private val retryStrategy: RetryStrategy<Inputs, Events, State> = FixedRetryStrategy(),
    private val deadLetterQueue: DeadLetterQueue<Inputs, Events, State> = IgnoreDeadLetterQueue(),
    private val idAccessor: InputId<Inputs, Events, State> = ReflectiveInputId(),
    private val journeyAccessor: InputJourney<Inputs, Events, State> = FixedInputJourney(),
    private val inputDelay: InputDelay<Inputs, Events, State> = FixedInputDelay(),
    private val priorityAccessor: InputPriority<Inputs, Events, State> = FixedInputPriority(),
    private val lock: QueueLock = InMemoryQueueLock(),
    filter: InputFilter<Inputs, Events, State>? = null,
) : ChannelInputStrategy<Inputs, Events, State>(
    capacity = Channel.BUFFERED,
    onBufferOverflow = BufferOverflow.SUSPEND,
    filter = filter,
), JobQueueInputStrategy<Inputs, Events, State> {
    override suspend fun InputStrategyScope<Inputs, Events, State>.processInputs(filteredQueue: Flow<Queued<Inputs, Events, State>>) {
        supervisorScope {
            // serialize items and send the results to the queue driver
            val job1 = serializeAndEnqueueInputs(
                queueName = queueName,
                filteredQueue = filteredQueue,
                inputSerializer = inputSerializer,
                json = json,
                clock = clock,
                driver = driver,
                idAccessor = idAccessor,
                journeyAccessor = journeyAccessor,
                inputDelay = inputDelay,
                priorityAccessor = priorityAccessor,
            )

            val job2 = pollAndProcessQueuedItems(
                queueName = queueName,
                clock = clock,
                inputSerializer = inputSerializer,
                json = json,
                driver = driver,
                throttle = throttle,
                timeout = timeout,
                retryStrategy = retryStrategy,
                deadLetterQueue = deadLetterQueue,
                acceptQueuedItem = { next, input ->
                    lock.lockJob(next.jobId)
                    lock.lockJourney(next.journeyId)

                    // restore the state for this input
                    val initialStateJson = stateStore.getState(next.queueName, next.journeyId)
                    val initialState = initialStateJson
                        ?.let { json.decodeFromString(stateSerializer, initialStateJson) }
                        ?: defaultState()
                    setState(initialState)

                    // process the input
                    val closedSuccessfully = processInput(input)

                    // persist the updated state
                    if (closedSuccessfully) {
                        // restore the state for this input
                        val serializedState = json.encodeToString(stateSerializer, getCurrentState())
                        stateStore.saveState(next.queueName, next.journeyId, serializedState)
                    }

                    // clear the state, so nothing dangerous accidentally gets left around in memory
                    setState(defaultState())

                    lock.unlockJourney(next.journeyId)
                    lock.unlockJob(next.jobId)

                    closedSuccessfully
                },
            )

            joinAll(job1, job2)
        }
    }

    private suspend fun InputStrategyScope<Inputs, Events, State>.processInput(input: Inputs): Boolean {
        val guardian = Guardian()
        CompletableDeferred<Unit>().also {
            acceptQueued(Queued.HandleInput(it, input), guardian) {
                rollbackState(defaultState())
            }
        }.await()
        return guardian.closedSuccessfully
    }

    private suspend fun InputStrategyScope<Inputs, Events, State>.setState(state: State) {
        CompletableDeferred<Unit>().also {
            acceptQueued(Queued.RestoreState(it, state), Guardian()) {
                rollbackState(defaultState())
            }
        }.await()
    }

    class Guardian : InputStrategy.Guardian by DefaultGuardian() {
        public var closedSuccessfully: Boolean = false

        override fun close() {
            super.close()
            closedSuccessfully = true
        }
    }
}

