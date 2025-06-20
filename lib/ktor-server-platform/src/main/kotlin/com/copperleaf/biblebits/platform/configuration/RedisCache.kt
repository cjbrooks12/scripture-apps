package com.copperleaf.biblebits.platform.configuration

import com.ucasoft.ktor.simpleCache.SimpleCache
import com.ucasoft.ktor.simpleRedisCache.redisCache
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.KoinApplication
import kotlin.time.Duration.Companion.seconds

fun Application.configureRedisCache(koinApplication: KoinApplication) {
    install(SimpleCache) {
        redisCache {
            invalidateAt = 10.seconds
            host = "localhost"
            port = 6379
        }
    }
}
