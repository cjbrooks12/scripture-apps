package com.caseyjbrooks.platform.services.authorization

import io.ktor.server.application.ApplicationCall

interface AuthorizationProvider {
    val userId: ApplicationCall.() -> String?
    val roles: ApplicationCall.() -> List<String>?

    suspend fun checkAuthorization(
        request: AuthorizationRequest
    ): Result<Boolean>
}
