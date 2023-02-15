package com.caseyjbrooks.scripturenow.utils

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

public class CachedSerializer<T : Any>(
    private val delegate: KSerializer<T>
) : KSerializer<Cached<T>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Cached") {
        element<String>("state")
        element("value", delegate.descriptor)
    }

    override fun serialize(encoder: Encoder, value: Cached<T>) {
        encoder.encodeStructure(descriptor) {
            val cachedState = when (value) {
                is Cached.NotLoaded -> "NotLoaded"
                is Cached.Fetching -> "Fetching"
                is Cached.FetchingFailed -> "FetchingFailed"
                is Cached.Value -> "Value"
            }
            encodeStringElement(descriptor, 0, cachedState)

            val cachedValue = value.getCachedOrNull()
            if (cachedValue != null) {
                encodeSerializableElement(descriptor, 1, delegate, cachedValue)
            }
        }
    }

    override fun deserialize(decoder: Decoder): Cached<T> {
        return decoder.decodeStructure(descriptor) {
            var state: String? = null
            var value: T? = null

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> state = decodeStringElement(descriptor, 0)
                    1 -> value = decodeSerializableElement(descriptor, 1, delegate)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }

            when (state) {
                "NotLoaded" -> Cached.NotLoaded(value)
                "Fetching" -> Cached.Fetching(value)
                "FetchingFailed" -> Cached.FetchingFailed(RuntimeException(), value)
                "Value" -> Cached.Value(value!!)
                else -> error("unknown cached state: $state")
            }
        }
    }
}
