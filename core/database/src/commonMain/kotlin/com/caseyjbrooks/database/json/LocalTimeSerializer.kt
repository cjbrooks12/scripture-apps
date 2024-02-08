package com.caseyjbrooks.database.json

import kotlinx.datetime.LocalTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeString("${value.hour}:${value.minute}:${value.second}")
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        val string = decoder.decodeString()
        val (hour, minute, second) = string.split(':')
        return LocalTime(hour.toInt(), minute.toInt(), second.toInt())
    }
}
