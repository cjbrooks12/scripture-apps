package com.copperleaf.abide

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()
actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient {
    TODO("Not yet implemented")
}
