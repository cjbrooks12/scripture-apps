package com.caseyjbrooks.platform.di

import com.caseyjbrooks.platform.configuration.connections.RedisConnection
import com.caseyjbrooks.platform.configuration.models.AuthorizationDriver
import com.caseyjbrooks.platform.configuration.models.BlobStorageDriver
import com.caseyjbrooks.platform.services.AuthorizationService
import com.caseyjbrooks.platform.services.BlobStorageService
import com.caseyjbrooks.platform.services.CacheService
import com.caseyjbrooks.platform.services.PubSubService
import com.caseyjbrooks.platform.services.authorization.OpenFgaAuthorizationService
import com.caseyjbrooks.platform.services.blob.FilesystemBlobStorageService
import com.caseyjbrooks.platform.services.cache.InMemoryCacheService
import com.caseyjbrooks.platform.services.cache.RedisCacheService
import com.caseyjbrooks.platform.services.pubsub.InMemoryPubSubService
import com.caseyjbrooks.platform.services.pubsub.RedisPubSubService
import com.caseyjbrooks.platform.util.extractConfiguration
import io.ktor.server.application.ApplicationEnvironment
import io.lettuce.core.RedisClient
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlinx.datetime.Clock
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.scope.Scope
import org.koin.dsl.module

val platformKoinModule: Module = module {
    // general
    single<Clock> { Clock.System }
    single<Json> { Json.Default }
    single<PrometheusMeterRegistry> { PrometheusMeterRegistry(PrometheusConfig.DEFAULT) }

    // connections
    single<RedisClient> {
        val connection = config<RedisConnection>("connections.redis")
        RedisClient.create(connection.redisConnectionString)
    }

    // cache
    single<CacheService>(createdAtStart = true) {
        when (val driver = config<CacheService.Driver>("services.cache")) {
            is CacheService.Driver.InMemory -> InMemoryCacheService()
            is CacheService.Driver.Redis -> RedisCacheService(get())
        }
    }

    // database
    single<AuthorizationService>(createdAtStart = true) {
        when (val driver = config<AuthorizationDriver>("services.authorization")) {
            is AuthorizationDriver.OpenFga -> {
                OpenFgaAuthorizationService()
            }
        }
    }

    // blob storage
    single<BlobStorageService>(createdAtStart = true) {
        when (val driver = config<BlobStorageDriver>("services.blob")) {
            is BlobStorageDriver.Filesystem -> {
                val rootDir = Path(driver.path)
                val fileSystem = SystemFileSystem
                if (!fileSystem.exists(rootDir)) {
                    fileSystem.createDirectories(rootDir)
                }

                FilesystemBlobStorageService(
                    fileSystem,
                    fileSystem.resolve(fileSystem.resolve(rootDir))
                )
            }
        }
    }

    // pub-sub
    single<PubSubService>(createdAtStart = true) {
        when (val driver = config<PubSubService.Driver>("services.pubsub")) {
            is PubSubService.Driver.InMemory -> InMemoryPubSubService()
            is PubSubService.Driver.Redis -> RedisPubSubService(get(), get())
        }
    }
}

public inline fun <reified T> Scope.config(path: String): T {
    return get<ApplicationEnvironment>().config.extractConfiguration<T>(path)
}
