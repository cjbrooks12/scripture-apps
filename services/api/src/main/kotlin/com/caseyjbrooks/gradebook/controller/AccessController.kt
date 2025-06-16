package com.caseyjbrooks.gradebook.controller

import com.caseyjbrooks.dto.AccessPath
import com.caseyjbrooks.dto.AppResource
import com.caseyjbrooks.dto.InviteAccessRequestDto
import com.caseyjbrooks.dto.InviteAccessResponseDto
import com.caseyjbrooks.dto.ListMembersRequestDto
import com.caseyjbrooks.dto.ListMembersResponseDto
import com.caseyjbrooks.dto.RevokeAccessRequestDto
import com.caseyjbrooks.platform.services.AuthorizationService
import com.caseyjbrooks.platform.util.AppResponse
import com.caseyjbrooks.platform.util.ok
import io.ktor.server.application.ApplicationCall
import org.koin.core.parameter.parametersOf
import org.koin.ktor.plugin.scope

class AccessController(
    private val authorizationService: AuthorizationService,
) {
    suspend fun inviteAccess(
        path: AccessPath,
        body: InviteAccessRequestDto,
    ): AppResponse.Json<InviteAccessResponseDto> {
        val openFgaUser = "user:${body.userId}"
        authorizationService
            .insertTuple(
                user = openFgaUser,
                relation = body.role.openfgaRelationName,
                objectType = path.objectType.openfgaObjectName,
                objectId = path.objectId.toString(),
            )
            .getOrThrow()

        return ok(
            InviteAccessResponseDto(
                objectType = path.objectType,
                objectId = path.objectId,
                user = openFgaUser,
            )
        )
    }

    suspend fun revokeAccess(
        path: AccessPath,
        body: RevokeAccessRequestDto,
    ): AppResponse.Json<Boolean> {
        authorizationService
            .removeTuple(
                user = "user:${body.userId}",
                relation = body.role.openfgaRelationName,
                objectType = path.objectType.openfgaObjectName,
                objectId = path.objectId.toString(),
            )
            .getOrThrow()

        return ok(true)
    }

    suspend fun listMembers(
        path: AccessPath,
        query: ListMembersRequestDto,
    ): AppResponse.Json<ListMembersResponseDto> {
        val users = authorizationService
            .listUsersForObject(
                objectType = path.objectType.openfgaObjectName,
                objectId = path.objectId.toString(),
            )
            .getOrThrow()

        val creators = users.filter { it.relation == AppResource.Role.Creator.openfgaRelationName }
        val viewers = users.filter { it.relation == AppResource.Role.Viewer.openfgaRelationName }

        return ok(
            ListMembersResponseDto(
                objectType = path.objectType,
                objectId = path.objectId,
                creators = creators.map { ListMembersResponseDto.Member(it.userId) },
                viewers = viewers.map { ListMembersResponseDto.Member(it.userId) },
            )
        )
    }


    companion object {
        operator fun invoke(call: ApplicationCall): AccessController {
            return call.scope.get<AccessController> { parametersOf(call) }
        }
    }
}
