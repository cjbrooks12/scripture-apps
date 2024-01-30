package com.caseyjbrooks.api

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformApiModule(): Module = module {
    factory<HttpClientEngineFactory<*>> {
        Js
    }
}

internal actual fun getFakePlatformApiModule(): Module = module {}
