package com.copperleaf.biblebits.platform.services.authorization

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.BaseApplicationPlugin
import io.ktor.server.application.install
import io.ktor.server.application.pluginOrNull
import io.ktor.util.AttributeKey

// Top-level Plugin
// ---------------------------------------------------------------------------------------------------------------------

class AuthorizationConfig {
    var providers: List<AuthorizationProvider<*>> = emptyList()

    internal fun copy(): AuthorizationConfig = AuthorizationConfig().also { config ->
        config.providers = this.providers
            .map { provider -> provider.copy() }
    }

    inline fun <reified T : AuthorizationProvider<*>> addProvider(provider: T) {
        providers += provider
    }

    suspend inline fun <reified T : AuthorizationProvider<*>> getProvider(
        call: ApplicationCall,
        name: String? = null
    ): T? {
        return providers
            .filterIsInstance<T>()
            .singleOrNull { provider -> provider.name == name }
    }
}

class Authorization(internal var config: AuthorizationConfig) {

    /**
     * Configures an already installed plugin.
     */
    fun configure(block: AuthorizationConfig.() -> Unit) {
        val newConfiguration = config.copy()
        block(newConfiguration)
        config = newConfiguration.copy()
    }

    /**
     * An installation object of the [Authorization] plugin.
     */
    companion object : BaseApplicationPlugin<Application, AuthorizationConfig, Authorization> {
        override val key: AttributeKey<Authorization> = AttributeKey("AuthorizationHolder")

        override fun install(pipeline: Application, configure: AuthorizationConfig.() -> Unit): Authorization {
            val config = AuthorizationConfig().apply(configure)
            return Authorization(config)
        }
    }
}

fun Application.authorization(block: AuthorizationConfig.() -> Unit) {
    pluginOrNull(Authorization)?.configure(block) ?: install(Authorization, block)
}
