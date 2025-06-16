package com.caseyjbrooks.platform.util

import io.ktor.server.routing.Route
import io.ktor.server.websocket.DefaultWebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


inline fun <reified T : Any> Route.readOnlyWebsocket(
    path: String,
    protocol: String? = null,
    crossinline flow: suspend DefaultWebSocketServerSession.() -> Flow<T>,
) {
    webSocket(path = path, protocol = protocol) {
        coroutineScope {
            val outgoingJob = launch {
                coroutineScope {
                    flow()
                        .onEach { sendSerialized<T>(it) }
                        .launchIn(this)
                }

                incoming.cancel()
            }
            launch {
                try {
                    for (frame in incoming) {
                        // ignore
                    }
                } finally {
                    outgoingJob.cancel()
                }
            }
        }
    }
}
