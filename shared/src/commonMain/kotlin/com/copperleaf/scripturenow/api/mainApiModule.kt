package com.copperleaf.scripturenow.api

import com.copperleaf.scripturenow.utils.KermitKtorLogger
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.singleton
import kotlin.time.Duration.Companion.seconds

fun mainApiModule() = DI.Module(name = "API >> Main") {
    bind<HttpClient> {
        singleton {
            HttpClient(instance<HttpClientEngineFactory<*>>()) {
                install(HttpCookies)
                install(HttpCache)
                install(HttpTimeout) {
                    requestTimeoutMillis = 30.seconds.inWholeMilliseconds
                    connectTimeoutMillis = 5.seconds.inWholeMilliseconds
                    socketTimeoutMillis = 5.seconds.inWholeMilliseconds
                }
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                    })
                }
                install(Logging) {
                    logger = KermitKtorLogger(instance(arg = "Ktor"))
                    level = LogLevel.ALL
                }
            }
        }
    }
    bind<Ktorfit> {
        factory { arg: String ->
            Ktorfit(
                baseUrl = arg,
                httpClient = instance()
            )
        }
    }
}
