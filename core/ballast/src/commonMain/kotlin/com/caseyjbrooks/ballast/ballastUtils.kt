package com.caseyjbrooks.ballast

import com.copperleaf.ballast.BallastLogger
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.scheduler.SchedulerInterceptor
import com.copperleaf.ballast.typedBuilder
import com.copperleaf.ballast.undo.BallastUndoInterceptor
import com.copperleaf.ballast.undo.state.StateBasedUndoController
import kotlinx.coroutines.Dispatchers
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

internal typealias TB<Inputs, Events, State> = BallastViewModelConfiguration.TypedBuilder<Inputs, Events, State>

public fun <Inputs : Any, Events : Any, State : Any> Scope.buildWithViewModel(
    initialState: State,
    inputHandler: InputHandler<Inputs, Events, State>,
    name: String,
    withSchedules: Boolean = false,
    withUndoRedo: Boolean = false,
    configureBuilder: (TB<Inputs, Events, State>) -> TB<Inputs, Events, State> = { it },
    bootstrappedInput: (() -> Inputs)? = null
): BallastViewModelConfiguration<Inputs, Events, State> {
    return BallastViewModelConfiguration.Builder()
        .typedBuilder<Inputs, Events, State>()
        .apply {
            this.initialState = initialState
            this.inputHandler = inputHandler
            this.name = name

            inputStrategy = FifoInputStrategy.typed()

            logger = { tag -> get<BallastLogger> { parametersOf(tag) } }
            this += LoggingInterceptor(logDebug = true, logInfo = true, logError = true)

            if (withSchedules) {
                this += SchedulerInterceptor()
            }
            if (withUndoRedo) {
                this += BallastUndoInterceptor(
                    StateBasedUndoController()
                )
            }

            if (bootstrappedInput != null) {
                this += BootstrapInterceptor { bootstrappedInput() }
            }
        }
        .dispatchers(
            inputsDispatcher = Dispatchers.Main,
            eventsDispatcher = Dispatchers.Main,
            sideJobsDispatcher = Dispatchers.Default,
            interceptorDispatcher = Dispatchers.Default,
        )
        .let(configureBuilder)
        .build()
}
