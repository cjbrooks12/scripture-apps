package com.copperleaf.scripturenow.repositories.auth

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.bus.observeInputsFromBus
import com.copperleaf.scripturenow.api.auth.AuthenticationProvider
import kotlinx.coroutines.flow.map

class AuthRepositoryInputHandler(
    private val eventBus: EventBus,
    private val authenticationProvider: AuthenticationProvider,
) : InputHandler<
    AuthRepositoryContract.Inputs,
    Any,
    AuthRepositoryContract.State> {
    override suspend fun InputHandlerScope<
        AuthRepositoryContract.Inputs,
        Any,
        AuthRepositoryContract.State>.handleInput(
        input: AuthRepositoryContract.Inputs
    ) = when (input) {
        is AuthRepositoryContract.Inputs.ClearCaches -> {
            updateState { AuthRepositoryContract.State() }
        }
        is AuthRepositoryContract.Inputs.Initialize -> {
            val previousState = getCurrentState()

            if (!previousState.initialized) {
                updateState { it.copy(initialized = true) }
                // start observing flows here
                logger.debug("initializing")
                observeFlows(
                    key = "Observe account changes",
                    eventBus
                        .observeInputsFromBus<AuthRepositoryContract.Inputs>(),
                    authenticationProvider
                        .getAuthState()
                        .map { AuthRepositoryContract.Inputs.AuthStateChanged(it) }
                )
            } else {
                logger.debug("already initialized")
                noOp()
            }
        }
        is AuthRepositoryContract.Inputs.AuthStateChanged -> {
            updateState { it.copy(authState = input.authState) }
        }
        is AuthRepositoryContract.Inputs.RequestSignOut -> {
            sideJob("sign out") {
                authenticationProvider.signOut()
            }
        }
    }
}
