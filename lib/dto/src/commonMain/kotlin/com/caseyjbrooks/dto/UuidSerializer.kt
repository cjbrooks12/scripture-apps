package com.caseyjbrooks.dto

import com.benasher44.uuid.uuidFrom
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import com.benasher44.uuid.Uuid as BAUuid

typealias Uuid = @Serializable(UuidSerializer::class) BAUuid

object UuidSerializer : KSerializer<BAUuid> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Uuid", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: BAUuid) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): BAUuid = uuidFrom(decoder.decodeString())
}
