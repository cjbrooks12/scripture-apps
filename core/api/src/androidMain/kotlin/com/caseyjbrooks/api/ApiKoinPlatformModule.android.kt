package com.caseyjbrooks.api

import com.caseyjbrooks.di.KoinModule
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

public actual class ApiKoinPlatformModule : KoinModule {
    override fun mainModule(): Module = module {
        factory<HttpClientEngineFactory<*>> {
            OkHttp
        }
    }
}
