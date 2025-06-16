package com.caseyjbrooks.platform.configuration

import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.Routing
import org.koin.core.KoinApplication

fun Routing.configureCors(koinApplication: KoinApplication) {
    install(CORS) {
        anyHost()
    }
}
