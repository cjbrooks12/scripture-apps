package com.caseyjbrooks.dto

import kotlinx.serialization.Serializable

@Serializable
data class AccessPath(
    val objectType: AppResource,
    val objectId: Uuid,
)

@Serializable
data class InviteAccessRequestDto(
    val userId: String,
    val role: AppResource.Role,
)
@Serializable
data class InviteAccessResponseDto(
    val objectType: AppResource,
    val objectId: Uuid,
    val user: String,
)

@Serializable
data class RevokeAccessRequestDto(
    val userId: String,
    val role: AppResource.Role,
)

@Serializable
class ListMembersRequestDto

@Serializable
data class ListMembersResponseDto(
    val objectType: AppResource,
    val objectId: Uuid,
    val creators: List<Member>,
    val viewers: List<Member>,
) {
    @Serializable
    data class Member(
        val userId: String,
    )
}
