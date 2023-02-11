package com.caseyjbrooks.scripturenow.repositories.auth

public fun interface AuthRepositoryProvider {
    public fun getAuthRepository(): AuthRepository
}
