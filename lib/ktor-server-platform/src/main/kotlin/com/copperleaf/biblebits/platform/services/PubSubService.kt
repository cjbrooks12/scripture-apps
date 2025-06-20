package com.copperleaf.biblebits.platform.services

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

interface PubSubService{
    suspend fun publish(key: String, event: Event)
    suspend fun subscribe(key: String, terminateOnClose: Boolean): Flow<Event>

    @Serializable
    sealed interface Event {
        @Serializable @SerialName("message") data class Message(val message: String) : Event
        @Serializable @SerialName("close") data object Close : Event
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    @JsonClassDiscriminator("driver")
    sealed interface Driver {
        @Serializable
        @SerialName("InMemory")
        data object InMemory : Driver

        @Serializable
        @SerialName("Redis")
        data object Redis : Driver
    }

}
