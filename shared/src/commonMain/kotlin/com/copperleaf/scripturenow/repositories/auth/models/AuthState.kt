package com.copperleaf.scripturenow.repositories.auth.models

sealed interface AuthState {
    data class SignedIn(
        val method: AuthenticationMethod,
        val displayName: String,
        val email: String,
        val isEmailVerified: Boolean,
        val photoUrl: String,
    ) : AuthState

    object SignedOut : AuthState

    enum class AuthenticationMethod {
        Anonymous, EmailAndPassword, Phone, Google
    }
}
