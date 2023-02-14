package com.caseyjbrooks.scripturenow.utils.converter

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

public class ToJsonConverter<T>(
    private val serializer: KSerializer<T>,
    private val json: Json = Json,
) : Converter<T, JsonElement> {
    override fun convertValue(from: T): JsonElement {
        return json.encodeToJsonElement(serializer, from)
    }
}
