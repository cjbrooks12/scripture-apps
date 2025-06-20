package com.copperleaf.biblebits.platform.services.cache

import com.copperleaf.biblebits.platform.services.CacheService
import kotlin.time.Duration

internal class InMemoryCacheService : CacheService {
    private val map = mutableMapOf<String, String>()

    override suspend fun get(key: String): String? {
        return map[key]
    }

    override suspend fun set(key: String, value: String) {
        map[key] = value
    }

    override suspend fun expire(key: String, duration: Duration) {
        // ignore for now
    }
}
