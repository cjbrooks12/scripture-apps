package com.caseyjbrooks.platform.services.authorization

import com.caseyjbrooks.platform.services.AuthorizationService
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.readText
import kotlinx.datetime.Instant
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class OpenFgaAuthorizationService(
    private val client: HttpClient,
    private val host: String,
    private val storeId: String,
    private val authorizationModelId: String,
) : AuthorizationService {

// Check (https://openfga.dev/api/service#/Relationship%20Queries/Check)
// ---------------------------------------------------------------------------------------------------------------------

    override suspend fun canAccess(
        user: String,
        relation: String,
        objectType: String,
        objectId: String,
    ): Result<Boolean> {
        return openFgaRequest<OpenFgaCheckRequest, OpenFgaCheckResponse>("/check") {
            OpenFgaCheckRequest(
                tupleKey = TupleKey(
                    user = user,
                    relation = relation,
                    _object = "$objectType:$objectId",
                ),
                authorizationModelId = authorizationModelId
            )
        }.map { response ->
            response.allowed
        }
    }

// Write (https://openfga.dev/api/service#/Relationship%20Tuples/Write)
// ---------------------------------------------------------------------------------------------------------------------

    override suspend fun insertTuple(
        user: String,
        relation: String,
        objectType: String,
        objectId: String,
    ): Result<Unit> {
        return openFgaRequest<OpenFgaWriteRequest, JsonObject>("/write") {
            OpenFgaWriteRequest(
                writes = TupleKeys(listOf(TupleKey(user, relation, objectType, objectId))),
                authorizationModelId = authorizationModelId,
            )
        }.map {

        }
    }

    override suspend fun removeTuple(
        user: String,
        relation: String,
        objectType: String,
        objectId: String,
    ): Result<Unit> {
        return openFgaRequest<OpenFgaWriteRequest, JsonObject>("/write") {
            OpenFgaWriteRequest(
                deletes = TupleKeys(listOf(TupleKey(user, relation, objectType, objectId))),
                authorizationModelId = authorizationModelId,
            )
        }.map {

        }
    }

    override suspend fun removeAllRelationsForObject(
        relations: List<String>,
        objectType: String,
        objectId: String,
    ): Result<Unit> {
        return openFgaRequest<OpenFgaReadRequest, OpenFgaReadResponse>("/read") {
            OpenFgaReadRequest(
                tupleKey = ObjectTuple("$objectType:$objectId"),
                authorizationModelId = authorizationModelId,
            )
        }.map { response ->
            openFgaRequest<OpenFgaWriteRequest, JsonObject>("/write") {
                OpenFgaWriteRequest(
                    deletes = TupleKeys(
                        response.tuples.map { tuple ->
                            TupleKey(
                                user = tuple.key.user,
                                relation = tuple.key.relation,
                                _object = tuple.key._object,
                            )
                        }
                    ),
                    authorizationModelId = authorizationModelId,
                )
            }.map {}
        }
    }

// List-users (https://openfga.dev/api/service#/Relationship%20Queries/ListUsers)
// ---------------------------------------------------------------------------------------------------------------------

    override suspend fun listUsersForObject(
        objectType: String,
        objectId: String,
    ): Result<List<AuthorizationService.UserRelation>> {
        return openFgaRequest<OpenFgaReadRequest, OpenFgaReadResponse>("/read") {
            OpenFgaReadRequest(
                tupleKey = ObjectTuple("$objectType:$objectId"),
                authorizationModelId = authorizationModelId,
            )
        }.map { response ->
            response.tuples.map { tuple ->
                AuthorizationService.UserRelation(
                    userId = tuple.key.user.removePrefix("user:"),
                    relation = tuple.key.relation,
                )
            }
        }
    }

// Internal
// ---------------------------------------------------------------------------------------------------------------------

    private suspend inline fun <reified RequestBody, reified Response> openFgaRequest(
        path: String,
        body: () -> RequestBody,
    ): Result<Response> {
        return runCatching {
            client
                .post {
                    url("$host/stores/$storeId$path")
                    contentType(ContentType.Application.Json)
                    setBody(body())
                }
                .body()
        }
    }

// API Models
// ---------------------------------------------------------------------------------------------------------------------

    @Serializable
    private data class TupleKey(
        val user: String,
        val relation: String,
        @SerialName("object") val _object: String,
    ) {
        companion object {
            operator fun invoke(
                user: String,
                relation: String,
                objectType: String,
                objectId: String,
            ) = TupleKey(user, relation, "$objectType:$objectId")
        }
    }

    @Serializable
    private data class TupleKeys(
        @SerialName("tuple_keys") val tupleKeys: List<TupleKey>,
    )

    @Serializable
    private data class Object(
        val type: String,
        val id: String,
    )

    @Serializable
    private data class ObjectTuple(
        @SerialName("object") val _object: String,
    )

    @Serializable
    private data class Filter(
        val type: String,
    )

    @Serializable
    private data class OpenFgaCheckRequest(
        @SerialName("tuple_key") val tupleKey: TupleKey,
        @SerialName("authorization_model_id") val authorizationModelId: String,
    )

    @Serializable
    data class OpenFgaCheckResponse(
        val allowed: Boolean
    )

    @Serializable
    private data class OpenFgaWriteRequest(
        val writes: TupleKeys? = null,
        val deletes: TupleKeys? = null,
        @SerialName("authorization_model_id") val authorizationModelId: String,
    )

    @Serializable
    private data class OpenFgaListUsersRequest(
        @SerialName("object") val _object: Object,
        val relation: String,
        @SerialName("user_filters") val userFilters: List<Filter>,
        @SerialName("authorization_model_id") val authorizationModelId: String,
    )

    @Serializable
    private data class OpenFgaListUsersResponse(
        val users: List<User>,
    ) {
        @Serializable
        data class User(
            @SerialName("object") val _object: Object,
        )
    }

    @Serializable
    private data class OpenFgaReadRequest(
        @SerialName("tuple_key") val tupleKey: ObjectTuple,
        @SerialName("authorization_model_id") val authorizationModelId: String,
    )

    @Serializable
    private data class OpenFgaReadResponse(
        val tuples: List<Tuple>,
    ) {
        @Serializable
        data class Tuple(
            val key: Key,
            val timestamp: Instant,
        ) {
            @Serializable
            data class Key(
                @SerialName("user") val user: String,
                @SerialName("relation") val relation: String,
                @SerialName("object") val _object: String,
            )
        }
    }

// Factory
// ---------------------------------------------------------------------------------------------------------------------

    companion object {
        operator fun invoke(): OpenFgaAuthorizationService {
            return OpenFgaAuthorizationService(
                client = HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                ignoreUnknownKeys = true
                            }
                        )
                    }
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    expectSuccess = true
                },
                host = "http://openfga:8080",
                storeId = SystemFileSystem
                    .source(Path("/openfga/fga_store.txt"))
                    .buffered()
                    .use { source -> source.readText().trim() },
                authorizationModelId = SystemFileSystem
                    .source(Path("/openfga/fga_authorization_model_id.txt"))
                    .buffered()
                    .use { source -> source.readText().trim() },
            )
        }
    }
}
