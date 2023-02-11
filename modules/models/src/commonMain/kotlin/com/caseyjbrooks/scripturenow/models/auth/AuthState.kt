package com.caseyjbrooks.scripturenow.models.auth

public sealed interface AuthState {
    public data class SignedIn(
        val method: AuthenticationMethod,
        val displayName: String,
        val email: String,
        val isEmailVerified: Boolean,
        val photoUrl: String,
    ) : AuthState

    public object SignedOut : AuthState

    public enum class AuthenticationMethod {
        Anonymous, EmailAndPassword, Phone, Google
    }
}
