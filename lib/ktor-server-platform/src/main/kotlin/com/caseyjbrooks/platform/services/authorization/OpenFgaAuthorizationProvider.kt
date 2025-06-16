package com.caseyjbrooks.platform.services.authorization

import com.caseyjbrooks.platform.services.AuthorizationService
import io.ktor.server.application.ApplicationCall

class OpenFgaAuthorizationProvider(
    private val service: AuthorizationService,
    override val userId: ApplicationCall.() -> String?,
    override val roles: ApplicationCall.() -> List<String>?,
) : AuthorizationProvider {

    override suspend fun checkAuthorization(
        request: AuthorizationRequest
    ): Result<Boolean> {
        val canAccess = service.canAccess(
            user = "user:${request.userId}",
            relation = request.resourceAction,
            objectType = request.routeResource,
            objectId = request.routeResourceId,
        )

        return canAccess
    }
}

fun AuthorizationConfig.openFga(
    service: AuthorizationService,
    userId: ApplicationCall.() -> String?,
    roles: ApplicationCall.() -> List<String>?,
) {
    provider = OpenFgaAuthorizationProvider(
        service = service,
        userId = userId,
        roles = roles,
    )
}
