package com.caseyjbrooks.platform.util.routing

import io.ktor.server.application.ApplicationCall
import io.ktor.server.routing.RoutingCall
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.reflect.KClass

class ExtractorScope(
    val call: RoutingCall,
)

fun interface Extractor<T> {
    operator fun invoke(scope: ExtractorScope): T
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T> getExtractor(registry: ExtractorRegistry): Extractor<T> {
    return registry.extractors[T::class] as? Extractor<T>
        ?: error("Extractor for type ${T::class} not registered")
}

class ExtractorRegistry {
    val extractors: Map<KClass<*>, Extractor<*>> = mapOf(
        ApplicationCall::class to ApplicationCallExtractor,
        RoutingCall::class to RoutingCallExtractor,
    )
}

suspend inline fun <reified T> RoutingCall.extractPath(): T {
    val json = Json
    val call = this
    val jsonObject = buildJsonObject {
        call.pathParameters.forEach { key, values ->
            when (values.size) {
                0 -> {
                    put(key, JsonNull)
                }

                1 -> {
                    put(key, JsonPrimitive(values.first()))
                }

                else -> {
                    put(key, JsonArray(values.map { JsonPrimitive(it) }))
                }
            }
        }
    }

    return json.decodeFromJsonElement<T>(jsonObject)
}

suspend inline fun <reified T> RoutingCall.extractQuery(): T {
    val json = Json {
        ignoreUnknownKeys = true
    }
    val call = this
    val jsonObject = buildJsonObject {
        call.queryParameters.forEach { key, values ->
            when (values.size) {
                0 -> {
                    put(key, JsonNull)
                }

                1 -> {
                    put(key, JsonPrimitive(values.first()))
                }

                else -> {
                    put(key, JsonArray(values.map { JsonPrimitive(it) }))
                }
            }
        }
    }

    return json.decodeFromJsonElement<T>(jsonObject)
}


suspend inline fun <reified T> ApplicationCall.extractPath(): T {
    val json = Json { ignoreUnknownKeys = true }
    val call = this
    val jsonObject = buildJsonObject {
        call.parameters.forEach { key, values ->
            when (values.size) {
                0 -> {
                    put(key, JsonNull)
                }

                1 -> {
                    put(key, JsonPrimitive(values.first()))
                }

                else -> {
                    put(key, JsonArray(values.map { JsonPrimitive(it) }))
                }
            }
        }
    }

    return json.decodeFromJsonElement<T>(jsonObject)
}
