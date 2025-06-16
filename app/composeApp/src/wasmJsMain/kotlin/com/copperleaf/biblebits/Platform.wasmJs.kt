package com.copperleaf.biblebits

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js
import io.ktor.client.plugins.defaultRequest
import kotlinx.browser.window

class WasmPlatform: Platform {
    override val name: String get() {
        return "Web with Kotlin/Wasm (${window.location.origin})"
    }
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
