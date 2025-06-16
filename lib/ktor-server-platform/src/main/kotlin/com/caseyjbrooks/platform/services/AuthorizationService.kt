package com.caseyjbrooks.platform.services

interface AuthorizationService {
    suspend fun canAccess(
        user: String,
        relation: String,
        objectType: String,
        objectId: String,
    ): Result<Boolean>

    suspend fun insertTuple(
        user: String,
        relation: String,
        objectType: String,
        objectId: String,
    ): Result<Unit>

    suspend fun removeTuple(
        user: String,
        relation: String,
        objectType: String,
        objectId: String,
    ): Result<Unit>

    suspend fun removeAllRelationsForObject(
        relations: List<String>,
        objectType: String,
        objectId: String,
    ): Result<Unit>

    suspend fun listUsersForObject(
        objectType: String,
        objectId: String,
    ): Result<List<UserRelation>>

    data class UserRelation(
        val userId: String,
        val relation: String,
    )
}
