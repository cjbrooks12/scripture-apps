package com.caseyjbrooks.platform.services.authorization

import com.caseyjbrooks.platform.services.authorization.OpenFgaAuthorizationService.TupleKey
import io.ktor.server.application.ApplicationCall

class OpenFgaAuthorizationProvider(
    private val service: OpenFgaAuthorizationService,
    override val userId: ApplicationCall.() -> String?,
    private val defaultContextualTuples: (String) -> List<TupleKey>,
) : AuthorizationProvider {

    override suspend fun checkAuthorization(
        request: AuthorizationRequest
    ): Result<Boolean> {
        val canAccess = service.canAccess(
            user = "user:${request.userId}",
            relation = request.resourceAction,
            objectType = request.routeResource,
            objectId = request.routeResourceId,
            contextualTuples = defaultContextualTuples(request.userId)
        )

        return canAccess
    }
}

fun AuthorizationConfig.openFga(
    service: OpenFgaAuthorizationService = OpenFgaAuthorizationService(),
    userId: ApplicationCall.() -> String?,
    defaultContextualTuples: (String) -> List<TupleKey>,
) {
    provider = OpenFgaAuthorizationProvider(
        service = service,
        userId = userId,
        defaultContextualTuples = defaultContextualTuples,
    )
}
