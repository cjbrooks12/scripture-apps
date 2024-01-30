package com.caseyjbrooks.api

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

internal expect fun getRealPlatformApiModule(): Module
public val realApiModule: Module = module {
    includes(getRealPlatformApiModule())
    single<HttpClient> {
        HttpClient(get<HttpClientEngineFactory<*>>()) {
            install(Logging) {
                logger = KtorKermitLogger(get<Logger> { parametersOf("HTTP") })
                level = LogLevel.ALL
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }
    }
}

internal expect fun getFakePlatformApiModule(): Module
public val fakeApiModule: Module = module {
    includes(getFakePlatformApiModule())
}
