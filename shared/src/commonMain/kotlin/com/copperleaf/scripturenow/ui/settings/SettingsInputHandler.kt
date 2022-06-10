package com.copperleaf.scripturenow.ui.settings

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.scripturenow.repositories.auth.AuthRepository
import kotlinx.coroutines.flow.map

class SettingsInputHandler(
    private val authRepository: AuthRepository,
) : InputHandler<
    SettingsContract.Inputs,
    SettingsContract.Events,
    SettingsContract.State> {
    override suspend fun InputHandlerScope<
        SettingsContract.Inputs,
        SettingsContract.Events,
        SettingsContract.State>.handleInput(
        input: SettingsContract.Inputs
    ) = when (input) {
        is SettingsContract.Inputs.Initialize -> {
            observeFlows(
                "auth state",
                authRepository
                    .getAuthState(input.forceRefresh)
                    .map { SettingsContract.Inputs.AuthStateChanged(it) }
            )
        }
        is SettingsContract.Inputs.AuthStateChanged -> {
            updateState { it.copy(authState = input.authState) }
        }
        is SettingsContract.Inputs.SignIn -> {
            postEvent(SettingsContract.Events.RequestSignIn)
        }
        is SettingsContract.Inputs.SignOut -> {
            sideJob("sign out") {
                authRepository.signOut()
            }
        }
        is SettingsContract.Inputs.GoBack -> {
            postEvent(SettingsContract.Events.NavigateUp)
        }
    }
}
