package com.copperleaf.biblebits.platform.services.authorization.openfga

import com.copperleaf.biblebits.platform.services.AuthorizationService
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.ObjectTuple
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.OpenFgaCheckRequest
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.OpenFgaCheckResponse
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.OpenFgaReadRequest
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.OpenFgaReadResponse
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.OpenFgaWriteRequest
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.TupleKey
import com.copperleaf.biblebits.platform.services.authorization.openfga.models.TupleKeys
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
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
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

    suspend fun canAccess(
        user: String,
        relation: String,
        objectType: String,
        objectId: String,
        contextualTuples: List<TupleKey>
    ): Result<Boolean> {
        return openFgaRequest<OpenFgaCheckRequest, OpenFgaCheckResponse>("/check") {
            OpenFgaCheckRequest(
                tupleKey = TupleKey(
                    user = user,
                    relation = relation,
                    _object = "$objectType:$objectId",
                ),
                authorizationModelId = authorizationModelId,
                contextualTuples = contextualTuples
                    .takeIf { it.isNotEmpty() }
                    ?.let { TupleKeys(it) }
            )
        }.map { response ->
            response.allowed
        }
    }

    override suspend fun canAccess(
        user: String,
        relation: String,
        objectType: String,
        objectId: String,
    ): Result<Boolean> {
        return canAccess(user, relation, objectType, objectId, emptyList())
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
                writes = TupleKeys(listOf(TupleKey.Companion(user, relation, objectType, objectId))),
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
                deletes = TupleKeys(listOf(TupleKey.Companion(user, relation, objectType, objectId))),
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
                    .source(kotlinx.io.files.Path("/openfga/fga_authorization_model_id.txt"))
                    .buffered()
                    .use { source -> source.readText().trim() },
            )
        }
    }
}
