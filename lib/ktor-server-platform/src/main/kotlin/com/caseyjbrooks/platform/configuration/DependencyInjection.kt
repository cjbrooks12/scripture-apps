package com.caseyjbrooks.platform.configuration

import io.ktor.server.application.Application
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.hooks.CallSetup
import io.ktor.server.application.hooks.ResponseSent
import io.ktor.server.application.install
import org.koin.core.KoinApplication
import org.koin.core.annotation.KoinInternalApi
import org.koin.ktor.plugin.KOIN_SCOPE_ATTRIBUTE_KEY
import org.koin.ktor.plugin.RequestScope
import org.koin.ktor.plugin.setKoinApplication

fun Application.configureDependencyInjection(koinApplication: KoinApplication) {
    install(KoinIsolatedNoMonitoring) {
        application(koinApplication)
    }
}

class KoinIsolatedNoMonitoringPluginConfiguration {
    internal var koin: KoinApplication? = null

    fun application(koin: KoinApplication) {
        this.koin = koin
    }
}

@OptIn(KoinInternalApi::class)
val KoinIsolatedNoMonitoring = createApplicationPlugin(
    name = "Koin",
    createConfiguration = ::KoinIsolatedNoMonitoringPluginConfiguration,
) {
    val koinApplication = pluginConfig.koin ?: error("Koin not configured!")
    application.setKoinApplication(koinApplication)

    // Scope Handling
    on(CallSetup) { call ->
        val scopeComponent = RequestScope(koinApplication.koin)
        call.attributes.put(KOIN_SCOPE_ATTRIBUTE_KEY, scopeComponent.scope)
    }
    on(ResponseSent) { call ->
        call.attributes[KOIN_SCOPE_ATTRIBUTE_KEY].close()
    }

    koinApplication.koin.logger.info("Koin is using Ktor isolated context")
}
