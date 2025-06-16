package com.caseyjbrooks.platform.services

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.time.Duration

interface CacheService {
    suspend fun get(key: String): String?
    suspend fun set(key: String, value: String)
    suspend fun expire(key: String, duration: Duration)

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
