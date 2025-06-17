package com.caseyjbrooks.platform.util

import io.ktor.server.config.ApplicationConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.decodeFromJsonElement

inline fun <reified T> ApplicationConfig.extractConfiguration(rootPath: String): T {
    val json = Json { ignoreUnknownKeys = true }
    val config = this.config(rootPath)
    val jsonObject = buildJsonObject {
        config.toMap().forEach { (key, value) ->
            put(key, value.toJsonElement())
        }
    }

    return json.decodeFromJsonElement<T>(jsonObject)
}

fun Any?.toJsonElement(): JsonElement {
    return when (this) {
        is String -> JsonPrimitive(this)
        is Boolean -> JsonPrimitive(this)
        is Int -> JsonPrimitive(this)
        is Long -> JsonPrimitive(this)
        is Float -> JsonPrimitive(this)
        is Double -> JsonPrimitive(this)

        is List<*> -> JsonArray(this.map { it.toJsonElement() })

        else -> error("Undefined data type: $this")
    }
}
