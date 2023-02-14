package com.caseyjbrooks.scripturenow.utils.converter

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

public class FromJsonConverter<T>(
    private val serializer: KSerializer<T>,
    private val json: Json = Json,
) : Converter<JsonElement, T> {
    override fun convertValue(from: JsonElement): T {
        return json.decodeFromJsonElement(serializer, from)
    }
}
