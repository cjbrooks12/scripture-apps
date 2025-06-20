package com.copperleaf.biblebits.platform.configuration

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.koin.core.KoinApplication

fun Application.configureSerialization(koinApplication: KoinApplication) {
    install(ContentNegotiation) {
        json()
    }
}
