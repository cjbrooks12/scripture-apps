package com.copperleaf.scripturenow.repositories.auth

import com.copperleaf.scripturenow.repositories.auth.models.AuthState

object AuthRepositoryContract {
    data class State(
        val initialized: Boolean = false,

        val authState: AuthState = AuthState.SignedOut,
    )

    sealed class Inputs {
        object ClearCaches : Inputs()
        object Initialize : Inputs()
        data class AuthStateChanged(val authState: AuthState): Inputs()
        object RequestSignOut : Inputs()
    }
}
