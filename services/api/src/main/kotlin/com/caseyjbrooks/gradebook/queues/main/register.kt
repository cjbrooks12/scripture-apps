package com.caseyjbrooks.gradebook.queues.main

import com.copperleaf.ballast.BallastViewModel
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.ExperimentalBallastApi
import com.copperleaf.ballast.ktor.BallastQueuePluginConfiguration
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.queue.inputstrategy.StatefulJobQueueInputStrategy
import com.copperleaf.ballast.scheduler.SchedulerInterceptor
import com.copperleaf.ballast.scheduler.schedule.FixedDelaySchedule
import io.ktor.server.application.Application
import io.ktor.util.AttributeKey
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

typealias MainServerQueueViewModel = BallastViewModel<
        ServerQueueContract.Inputs,
        ServerQueueContract.Events,
        ServerQueueContract.State>

val MainServerQueue = AttributeKey<MainServerQueueViewModel>("MainServerQueue")

@OptIn(ExperimentalBallastApi::class)
fun BallastQueuePluginConfiguration.mainServerQueue(
    application: Application,
) = with(application) {
    registerQueue(
        attributeKey = MainServerQueue,
        inputHandler = ServerQueueInputHandler(),
        inputStrategy = StatefulJobQueueInputStrategy(
            inputSerializer = ServerQueueContract.Inputs.serializer(),
            stateSerializer = ServerQueueContract.State.serializer(),
            defaultState = { ServerQueueContract.State() },
            timeout = {
                when (it) {
                    is ServerQueueContract.Inputs.Timeout -> it.duration / 2.0
                    else -> 30.seconds
                }
            },
            inputDelay = {
                when (it) {
                    is ServerQueueContract.Inputs.Delayed -> it.delay
                    else -> Duration.ZERO
                }
            },
        ),
        viewModelBuilder = BallastViewModelConfiguration.Builder()
            .apply {
                this += SchedulerInterceptor {
                    onSchedule(
                        key = "increment",
                        schedule = FixedDelaySchedule(30.seconds),
                        scheduledInput = { ServerQueueContract.Inputs.Increment },
                    )
                }
//                logger = ::PrintlnLogger
            }
    )
}
