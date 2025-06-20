package com.copperleaf.biblebits.routing

import com.caseyjbrooks.dto.AccessPath
import com.caseyjbrooks.dto.InviteAccessRequestDto
import com.caseyjbrooks.dto.ListMembersRequestDto
import com.caseyjbrooks.dto.RevokeAccessRequestDto
import com.copperleaf.biblebits.controller.AccessController
import com.copperleaf.biblebits.platform.services.authorization.openfga.authorizeOpenFga
import com.copperleaf.biblebits.platform.util.GET
import com.copperleaf.biblebits.platform.util.POST
import com.copperleaf.biblebits.platform.util.routing.extractPath
import com.copperleaf.biblebits.platform.util.routing.extractQuery
import io.ktor.server.request.receive
import io.ktor.server.routing.Route

private fun Route.authorize(
    action: String,
    build: Route.() -> Unit
) {
    authorizeOpenFga(
        routePluginName = action,
        {
        this.action = { action }
        this.resource = {
            extractPath<AccessPath>().objectType.openfgaObjectName
        }
        this.resourceId = {
            extractPath<AccessPath>().objectId.toString()
        }
    }, build)
}

fun Route.accessRouter() {
    authorize("can_manage_access") {
        POST("/{objectType}/{objectId}/invite") {
            AccessController.Companion(call).inviteAccess(
                path = call.extractPath<AccessPath>(),
                body = call.receive<InviteAccessRequestDto>(),
            )
        }
    }
    authorize("can_manage_access") {
        POST("/{objectType}/{objectId}/revoke") {
            AccessController.Companion(call).revokeAccess(
                path = call.extractPath<AccessPath>(),
                body = call.receive<RevokeAccessRequestDto>(),
            )
        }
    }
    authorize("can_manage_access") {
        GET("/{objectType}/{objectId}/members") {
            AccessController.Companion(call).listMembers(
                path = call.extractPath<AccessPath>(),
                query = call.extractQuery<ListMembersRequestDto>()
            )
        }
    }
}
