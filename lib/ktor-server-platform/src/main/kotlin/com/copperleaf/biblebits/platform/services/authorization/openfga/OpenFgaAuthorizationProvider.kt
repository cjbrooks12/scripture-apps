package com.copperleaf.biblebits.platform.services.authorization.openfga

import com.copperleaf.biblebits.platform.services.authorization.AuthorizationConfig
import com.copperleaf.biblebits.platform.services.authorization.AuthorizationProvider
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.TupleKey
import io.ktor.server.application.ApplicationCall

class OpenFgaAuthorizationProvider(
    override val name: String?,
    private val service: OpenFgaAuthorizationService,
    val userId: ApplicationCall.() -> String?,
    private val defaultContextualTuples: (String) -> List<TupleKey>,
) : AuthorizationProvider<OpenFgaAuthorizationRequest> {

    override suspend fun checkAuthorization(
        request: OpenFgaAuthorizationRequest,
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

    override fun copy(): OpenFgaAuthorizationProvider {
        return OpenFgaAuthorizationProvider(
            name = name,
            service = service,
            userId = userId,
            defaultContextualTuples = defaultContextualTuples,
        )
    }
}

fun AuthorizationConfig.openFga(
    name: String? = null,
    service: OpenFgaAuthorizationService = OpenFgaAuthorizationService.Companion(),
    userId: ApplicationCall.() -> String? = { null },
    defaultContextualTuples: (String) -> List<TupleKey> = { emptyList() },
) {
    addProvider(
        OpenFgaAuthorizationProvider(
            name = name,
            service = service,
            userId = userId,
            defaultContextualTuples = defaultContextualTuples,
        )
    )
}
