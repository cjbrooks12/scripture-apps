package com.copperleaf.biblebits.platform.services.authorization

interface AuthorizationProvider<T> {
    val name: String?

    suspend fun checkAuthorization(
        request: T
    ): Result<Boolean>

    fun copy(): AuthorizationProvider<T>
}
