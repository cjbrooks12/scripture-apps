package com.caseyjbrooks.scripturenow.repositories.auth

import com.caseyjbrooks.scripturenow.models.auth.AuthState

public object AuthRepositoryContract {
    public data class State(
        val initialized: Boolean = false,

        val authState: AuthState = AuthState.SignedOut,
    )

    public sealed class Inputs {
        public object ClearCaches : Inputs()
        public object Initialize : Inputs()
        public data class AuthStateChanged(val authState: AuthState) : Inputs()
        public object RequestSignOut : Inputs()
        public data class FirebaseInstallationIdUpdated(val firebaseInstallationId: String) : Inputs()
        public data class FirebaseTokenUpdated(val token: String) : Inputs()
    }

    public sealed class Events
}
