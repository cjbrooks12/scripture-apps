package com.copperleaf.biblebits.platform.services.cache

import com.copperleaf.biblebits.platform.services.CacheService
import io.lettuce.core.RedisClient
import io.lettuce.core.api.coroutines
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import kotlin.time.Duration
import kotlin.time.toJavaDuration

internal class RedisCacheService(
    private val redisClient: RedisClient,
) : CacheService {
    private val connection: RedisCoroutinesCommands<String, String> = redisClient.connect().coroutines()

    override suspend fun get(key: String): String? {
        return connection.get(key)
    }

    override suspend fun set(key: String, value: String) {
        connection.set(key, value)
    }

    override suspend fun expire(key: String, duration: Duration) {
        connection.expire(key, duration.toJavaDuration())
    }
}
