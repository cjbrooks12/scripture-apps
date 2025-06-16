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
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.queue.inputstrategy.JobQueueInputStrategy
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

data class BallastKtorQueue<Inputs : Any, Events : Any, State : Any>(
    val attributeKey: AttributeKey<BallastViewModel<Inputs, Events, State>>,
    val inputHandler: InputHandler<Inputs, Events, State>,
    val eventHandler: EventHandler<Inputs, Events, State>,
    val inputStrategy: JobQueueInputStrategy<Inputs, Events, State>,
    val viewModelBuilder: BallastViewModelConfiguration.Builder,
) {

    fun CoroutineScope.startProcessing(application: Application) {
        val vm = BasicViewModel(
            coroutineScope = this,
            config = viewModelBuilder
                .withViewModel(
                    initialState = inputStrategy.defaultState(),
                    inputHandler = inputHandler,
                    name = attributeKey.name
                )
                .also {
                    it.inputStrategy = inputStrategy
                }
                .build(),
            eventHandler = eventHandler,
        )

        application.attributes.put(attributeKey, vm)
    }
}

class BallastQueuePluginConfiguration {
    internal var queues: MutableList<BallastKtorQueue<*, *, *>> = mutableListOf()

    fun <Inputs : Any, Events : Any, State : Any> registerQueue(
        attributeKey: AttributeKey<BallastViewModel<Inputs, Events, State>>,
        inputHandler: InputHandler<Inputs, Events, State>,
        eventHandler: EventHandler<Inputs, Events, State> = eventHandler { },
        inputStrategy: JobQueueInputStrategy<Inputs, Events, State>,
        viewModelBuilder: BallastViewModelConfiguration.Builder = BallastViewModelConfiguration.Builder(),
    ) {
        queues += BallastKtorQueue(
            attributeKey = attributeKey,
            inputHandler = inputHandler,
            eventHandler = eventHandler,
            inputStrategy = inputStrategy,
            viewModelBuilder = viewModelBuilder,
        )
    }
}

val BallastQueue = createApplicationPlugin(
    name = "BallastQueue",
    createConfiguration = ::BallastQueuePluginConfiguration
) {
    on(MonitoringEvent(ApplicationStarted)) { application ->
        pluginConfig.queues.forEach { queue ->
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

inline fun <reified Inputs : Any, reified Events : Any, reified State : Any> ApplicationCall.queue(
    key: AttributeKey<BallastViewModel<Inputs, Events, State>>
): BallastViewModel<Inputs, Events, State> {
    return try {
        application.attributes[key]
    } catch (e: Exception) {
        e.printStackTrace()
        throw e
    }
}
