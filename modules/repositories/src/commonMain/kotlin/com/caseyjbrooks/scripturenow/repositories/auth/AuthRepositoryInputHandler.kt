package com.caseyjbrooks.scripturenow.repositories.auth

import com.caseyjbrooks.scripturenow.api.auth.Session
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

public class AuthRepositoryInputHandler(
    private val session: Session,
) : InputHandler<
    AuthRepositoryContract.Inputs,
    AuthRepositoryContract.Events,
    AuthRepositoryContract.State> {
    override suspend fun InputHandlerScope<
        AuthRepositoryContract.Inputs,
        AuthRepositoryContract.Events,
        AuthRepositoryContract.State>.handleInput(
        input: AuthRepositoryContract.Inputs
    ): Unit = when (input) {
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
                    session
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
                session.signOut()
            }
        }
    }
}
