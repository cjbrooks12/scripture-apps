package com.copperleaf.ballast.queue.inputstrategy

import com.copperleaf.ballast.InputStrategy
import com.copperleaf.ballast.InputStrategyScope
import com.copperleaf.ballast.Queued
import com.copperleaf.ballast.queue.DeadLetterQueue
import com.copperleaf.ballast.queue.InputDelay
import com.copperleaf.ballast.queue.InputId
import com.copperleaf.ballast.queue.InputJourney
import com.copperleaf.ballast.queue.InputPriority
import com.copperleaf.ballast.queue.InputTimeout
import com.copperleaf.ballast.queue.QueueDriver
import com.copperleaf.ballast.queue.QueueThrottle
import com.copperleaf.ballast.queue.RetryStrategy
import com.copperleaf.ballast.queue.SerializedJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
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
interface JobQueueInputStrategy<Inputs : Any, Events : Any, State : Any> : InputStrategy<Inputs, Events, State> {
    val defaultState: () -> State
}

fun <Inputs : Any, Events : Any, State : Any> InputStrategyScope<Inputs, Events, State>.serializeAndEnqueueInputs(
    queueName: String,
    filteredQueue: Flow<Queued<Inputs, Events, State>>,
    inputSerializer: KSerializer<Inputs>,
    json: Json,
    clock: Clock,
    driver: QueueDriver,
    journeyAccessor: InputJourney<Inputs, Events, State>,
    idAccessor: InputId<Inputs, Events, State>,
    inputDelay: InputDelay<Inputs, Events, State>,
    priorityAccessor: InputPriority<Inputs, Events, State>,
): Job {
    return launch {
        filteredQueue
            .onEach { queued ->
                when (queued) {
                    is Queued.HandleInput -> {
                        val input = queued.input
                        driver.addToQueue(
                            SerializedJob(
                                queueName = queueName,
                                journeyId = journeyAccessor.getJourneyId(input),
                                jobId = idAccessor.getId(input),
                                payload = json.encodeToString(inputSerializer, input),
                                priority = priorityAccessor.assignPriority(input),
                                insertedAt = clock.now(),
                                lastAttemptAt = null,
                                delay = inputDelay.inputDelay(input),
                                attempts = 0u,
                            )
                        )
                    }

                    is Queued.RestoreState -> {
                        acceptQueued(queued, StatefulJobQueueInputStrategy.Guardian()) { }
                    }

                    is Queued.ShutDownGracefully -> {
                        acceptQueued(queued, StatefulJobQueueInputStrategy.Guardian()) { }
                    }
                }
            }
            .launchIn(this)
    }
}

fun <Inputs : Any, Events : Any, State : Any> InputStrategyScope<Inputs, Events, State>.pollAndProcessQueuedItems(
    queueName: String,
    clock: Clock = Clock.System,
    inputSerializer: KSerializer<Inputs>,
    json: Json,
    driver: QueueDriver,
    throttle: QueueThrottle,
    timeout: InputTimeout<Inputs, Events, State>,
    retryStrategy: RetryStrategy<Inputs, Events, State>,
    deadLetterQueue: DeadLetterQueue<Inputs, Events, State>,
    acceptQueuedItem: suspend (SerializedJob, Inputs) -> Boolean
): Job {
    return launch {
        var emptyPollCount = 0
        while (true) {
            val next = driver.pollNext(queueName)

            if (next != null) {
                val jobId = "${next.queueName}:${next.jobId}"
                val inputJson = next.payload
                val input = json.decodeFromString(inputSerializer, inputJson)
                val timeoutDuration = timeout.getTimeout(input)
                val numberOfAttempts = next.attempts + 1u

                logger.debug("[$jobId] Processing (attempt ${next.attempts})")
                val success = withTimeoutOrNull(timeoutDuration) {
                    acceptQueuedItem(next, input)
                } ?: run {
                    logger.debug("[$jobId] Timed out after running for $timeoutDuration")
                    false
                }

                if (success) {
                    logger.debug("[$jobId] Job completed successfully")
                } else {
                    if (retryStrategy.shouldRetry(input, numberOfAttempts)) {
                        logger.debug("[$jobId] Job failed, retrying")
                        driver.addToQueue(
                            next.copy(
                                attempts = numberOfAttempts,
                                lastAttemptAt = clock.now(),
                            )
                        )
                    } else {
                        logger.debug("[$jobId] Job exceeded retries, sending to DeadLetterQueue")
                        deadLetterQueue.inputDied(input)
                    }
                }

                emptyPollCount = 0
            } else {
                emptyPollCount++
            }

            throttle.awaitNext(emptyPollCount)
        }
    }
}
