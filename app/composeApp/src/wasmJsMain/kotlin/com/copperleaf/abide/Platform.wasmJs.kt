package com.copperleaf.abide

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.defaultRequest

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Js) {
    config(this)

    defaultRequest {
        url("http://localhost:9001/")
    }

    engine {

    }
}
