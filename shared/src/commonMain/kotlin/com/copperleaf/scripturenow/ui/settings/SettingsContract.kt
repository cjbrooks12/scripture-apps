package com.copperleaf.scripturenow.ui.settings

import com.copperleaf.scripturenow.repositories.auth.models.AuthState

object SettingsContract {
    data class State(
        val authState: AuthState = AuthState.SignedOut,
    )

    sealed class Inputs {
        data class Initialize(val forceRefresh: Boolean) : Inputs()
        data class AuthStateChanged(val authState: AuthState) : Inputs()
        object SignIn : Inputs()
        object SignOut : Inputs()
        object GoBack : Inputs()
    }

    sealed class Events {
        object RequestSignIn : Events()
        object NavigateUp : Events()
    }
}
