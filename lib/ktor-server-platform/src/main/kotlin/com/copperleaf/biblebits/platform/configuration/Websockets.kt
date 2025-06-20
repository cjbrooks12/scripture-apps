package com.copperleaf.biblebits.platform.configuration

import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.WebSockets
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import kotlin.time.Duration.Companion.seconds

fun Application.configureWebsockets(koinApplication: KoinApplication) {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriodMillis = 10.seconds.inWholeMilliseconds
    }
}
