@file:OptIn(ExperimentalBallastApi::class)
@file:Suppress("UNCHECKED_CAST")

package com.copperleaf.ballast.ktor

import com.copperleaf.ballast.BallastViewModel
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.ExperimentalBallastApi
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.scheduler.SchedulerAdapter
import com.copperleaf.ballast.scheduler.SchedulerInterceptor
import com.copperleaf.ballast.withViewModel
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.MonitoringEvent
import io.ktor.util.AttributeKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

data class BallastKtorSchedule<Inputs : Any, Events : Any, State : Any>(
    val attributeKey: AttributeKey<BallastViewModel<Inputs, Events, State>>,
    val initialState: State,
    val inputHandler: InputHandler<Inputs, Events, State>,
    val eventHandler: EventHandler<Inputs, Events, State>,
    val scheduleAdapter: SchedulerAdapter<Inputs, Events, State>,
    val viewModelBuilder: BallastViewModelConfiguration.Builder,
) {
    fun CoroutineScope.startProcessing(application: Application) {
        val vm = BasicViewModel(
            coroutineScope = this,
            config = viewModelBuilder
                .withViewModel(
                    initialState = initialState,
                    inputHandler = inputHandler,
                    name = attributeKey.name
                )
                .also {
                    it.inputStrategy = FifoInputStrategy.typed()
                    it += SchedulerInterceptor(initialSchedule = scheduleAdapter)
                }
                .build(),
            eventHandler = eventHandler,
        )

        application.attributes.put(attributeKey, vm)
    }
}

class BallastSchedulesPluginConfiguration {
    internal var schedules: MutableList<BallastKtorSchedule<*, *, *>> = mutableListOf()

    fun <Inputs : Any, Events : Any, State : Any> registerSchedule(
        attributeKey: AttributeKey<BallastViewModel<Inputs, Events, State>>,
        initialState: State,
        inputHandler: InputHandler<Inputs, Events, State>,
        eventHandler: EventHandler<Inputs, Events, State> = eventHandler {  },
        scheduleAdapter: SchedulerAdapter<Inputs, Events, State>,
        viewModelBuilder: BallastViewModelConfiguration.Builder = BallastViewModelConfiguration.Builder(),
    ) {
        schedules += BallastKtorSchedule(
            attributeKey = attributeKey,
            initialState = initialState,
            inputHandler = inputHandler,
            eventHandler = eventHandler,
            scheduleAdapter = scheduleAdapter,
            viewModelBuilder = viewModelBuilder,
        )
    }
}

val BallastSchedules = createApplicationPlugin(
    name = "BallastSchedules",
    createConfiguration = ::BallastSchedulesPluginConfiguration
) {
    on(MonitoringEvent(ApplicationStarted)) { application ->
        pluginConfig.schedules.forEach { queue ->
            application.launch {
                supervisorScope {
                    with(queue) {
                        startProcessing(application)
                    }
                }
            }
        }
    }
}

inline fun <reified Inputs : Any, reified Events : Any, reified State : Any> ApplicationCall.schedule(
    key: AttributeKey<BallastViewModel<Inputs, Events, State>>
): BallastViewModel<Inputs, Events, State> {
    return try {
        application.attributes[key]
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}
