package com.caseyjbrooks.scripturenow.utils

import com.caseyjbrooks.scripturenow.models.VerseReference
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object VerseReferenceSerializer : KSerializer<VerseReference> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("VerseReference", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: VerseReference) {
        encoder.encodeString(value.referenceText)
    }

    override fun deserialize(decoder: Decoder): VerseReference {
        return decoder.decodeString().parseVerseReference()
    }
}
