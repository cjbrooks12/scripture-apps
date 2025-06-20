package com.copperleaf.biblebits.platform.configuration.connections

import kotlinx.serialization.Serializable

@Serializable
data class RedisConnection(
    val redisConnectionString: String,
)
