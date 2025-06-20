package com.copperleaf.biblebits.platform.services.database

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
annotation class DatabaseOperation(
    val readOne: Boolean = true,
    val readMany: Boolean = true,
    val create: Boolean = true,
    val update: Boolean = true,
)

@OptIn(ExperimentalSerializationApi::class)
@SerialInfo
annotation class PrimaryKey

@OptIn(ExperimentalSerializationApi::class)
class DatabaseSerializer() : AbstractEncoder() {
    val list = mutableListOf<Any>()

    override fun encodeNull() {
        // ignore
    }

    override val serializersModule: SerializersModule = EmptySerializersModule()

    override fun encodeValue(value: Any) {
        list.add(value)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return super.beginStructure(descriptor).also { println("beginStructure") }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        super.endStructure(descriptor).also { println("endStructure") }
    }
}

fun <T> encodeToDatabase(serializer: SerializationStrategy<T>, value: T): List<Any> {
    val encoder = DatabaseSerializer()
    encoder.encodeSerializableValue(serializer, value)
    return encoder.list
}

inline fun <reified T> encodeToDatabase(value: T) {
    println("creating record of ${T::class.simpleName}: value=$value")
    encodeToDatabase(serializer(), value)
}
