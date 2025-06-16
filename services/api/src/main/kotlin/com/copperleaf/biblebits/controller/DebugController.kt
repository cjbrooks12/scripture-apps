package com.copperleaf.biblebits.controller

import com.caseyjbrooks.platform.services.CacheService
import com.caseyjbrooks.platform.util.AppResponse
import com.caseyjbrooks.platform.util.ok
import io.ktor.http.Headers
import io.ktor.server.application.ApplicationCall
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.koin.core.parameter.parametersOf
import org.koin.ktor.plugin.scope
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.Duration.Companion.seconds

class DebugController(
    private val cacheService: CacheService,
) {

// Server Info
// ---------------------------------------------------------------------------------------------------------------------

    @Serializable
    public data class ServerInfoResponse(val version: String)

    public suspend fun serverInfo(): AppResponse.Json<ServerInfoResponse> {
        return ok(
            ServerInfoResponse(
                version = "0.1.0"
            )
        )
    }

// Authentication
// ---------------------------------------------------------------------------------------------------------------------

    @Serializable
    data class RequestHeaders(
        val headers: JsonObject
    )

    public suspend fun headers(
        headers: Headers
    ): AppResponse.Json<RequestHeaders> {
        val headersJson = buildJsonObject {
            val obj = this
            headers.forEach { key, values ->
                if (values.size == 1) {
                    obj.put(key, JsonPrimitive(values.single()))
                } else {
                    obj.put(key, JsonArray(values.map { JsonPrimitive(it) }))
                }
            }
        }

        return ok(RequestHeaders(headersJson))
    }

    @Serializable
    data class OidcCurrentUserInfo(
        @SerialName("Authorization") val authorization: JsonObject,
        @SerialName("X-ID-Token") val idToken: JsonObject,
        @SerialName("X-Userinfo") val userinfo: JsonObject,
        @SerialName("X-Refresh-Token") val refreshToken: JsonObject,
    )

    public suspend fun oidcCurrentUserInfo(
        headers: Headers
    ): AppResponse.Json<OidcCurrentUserInfo> {
        return ok(
            OidcCurrentUserInfo(
                authorization = parseOidcJwt(headers, "Authorization", "Bearer "),
                idToken = parseOidcHeader(headers, "X-ID-Token"),
                userinfo = parseOidcHeader(headers, "X-Userinfo"),
                refreshToken = parseOidcJwt(headers, "X-Refresh-Token"),
            )
        )
    }

    private fun parseOidcHeader(headers: Headers, headerName: String): JsonObject {
        return headers[headerName]
            ?.let { headerValueBase64 ->
                buildJsonObject {
                    put("raw", JsonPrimitive(headerValueBase64))
                    put("payload", base64ToJson(headerValueBase64))
                }
            }
            ?: run {
                buildJsonObject {
                    put("raw", JsonNull)
                    put("payload", JsonNull)
                }
            }
    }

    private fun parseOidcJwt(headers: Headers, headerName: String, headerValuePrefix: String = ""): JsonObject {
        return headers[headerName]
            ?.let { headerValueBase64 ->
                val (header, payload, signature) = headerValueBase64.removePrefix(headerValuePrefix).split(".")

                buildJsonObject {
                    put("raw", JsonPrimitive(headerValueBase64))
                    put("header", base64ToJson(header))
                    put("payload", base64ToJson(payload))
                    put("signature", JsonPrimitive(signature))
                }
            }
            ?: run {
                buildJsonObject {
                    put("raw", JsonNull)
                    put("header", JsonNull)
                    put("payload", JsonNull)
                    put("signature", JsonNull)
                }
            }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun base64ToJson(value: String): JsonElement {
        return try {
            Json
                .Default
                .decodeFromString(
                    JsonElement.serializer(),
                    Base64
                        .Default
                        .decode(value)
                        .toString(Charsets.UTF_8)
                )
        } catch (e: Exception) {
            JsonObject(emptyMap())
        }
    }

// Authorization
// ---------------------------------------------------------------------------------------------------------------------

// Database
// ---------------------------------------------------------------------------------------------------------------------

    @Serializable
    data class DatabaseTestResponse(
        val driver: String,
        val rows: List<Row>
    ) {
        @Serializable
        data class Row(
            val now: String,
        )
    }

    @Serializable
    private data class DatabaseTestDao(
        val now: String,
    )

    suspend fun databaseTest(
    ): AppResponse.Json<DatabaseTestResponse> {
        return ok(
            DatabaseTestResponse(
                driver = "",
                rows = emptyList()
            )
        )
    }

// Cache
// ---------------------------------------------------------------------------------------------------------------------

    @Serializable
    data class CacheTestResponse(
        val driver: String,
        val count: Int,
    )

    internal suspend fun cacheTest(
    ): AppResponse.Json<CacheTestResponse> {
        val key = "count"
        val currentValue = cacheService.get(key)?.toIntOrNull() ?: 0
        val newValue = currentValue + 1
        cacheService.set(key, newValue.toString())
        cacheService.expire(key, 30.seconds)

        return ok(
            CacheTestResponse(
                driver = cacheService::class.qualifiedName ?: "",
                count = newValue,
            )
        )
    }

// Pub Sub
// ---------------------------------------------------------------------------------------------------------------------

    companion object {
        operator fun invoke(call: ApplicationCall): DebugController {
            return call.scope.get<DebugController> { parametersOf(call) }
        }
    }
}
