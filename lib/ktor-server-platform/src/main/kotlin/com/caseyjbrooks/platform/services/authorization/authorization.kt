package com.caseyjbrooks.platform.services.authorization

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.BaseApplicationPlugin
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.application.pluginOrNull
import io.ktor.server.auth.AuthenticationChecked
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext
import io.ktor.util.AttributeKey

// Top-level Plugin
// ---------------------------------------------------------------------------------------------------------------------

class AuthorizationConfig {
    internal var provider: AuthorizationProvider? = null

    internal fun copy(): AuthorizationConfig = AuthorizationConfig().also {
        it.provider = this.provider
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


// Route-Scoped Plugin, which depends on the top-level plugin
// ---------------------------------------------------------------------------------------------------------------------

class AuthorizationRoutePluginConfiguration {
    lateinit var action: suspend ApplicationCall.() -> String
    lateinit var resource: suspend ApplicationCall.() -> String
    lateinit var resourceId: suspend ApplicationCall.() -> String
}

data class AuthorizationRequest(
    val userId: String,
    val routeResource: String,
    val routeResourceId: String,
    val resourceAction: String,
)

val AuthorizationRoute = createRouteScopedPlugin(
    name = "Authorization Route",
    createConfiguration = ::AuthorizationRoutePluginConfiguration
) {
    on(AuthenticationChecked) { call ->
        val authorization = call.application.pluginOrNull(Authorization)
        if (authorization == null) {
            call.respondText("Authorization plugin not configured", status = HttpStatusCode.InternalServerError)
            return@on
        }

        val authorizationProvider = authorization.config.provider
        if (authorizationProvider == null) {
            call.respondText("Authorization provider not configured", status = HttpStatusCode.InternalServerError)
            return@on
        }

        val userId = authorizationProvider.userId(call)
        if (userId == null) {
            call.respondText(
                "Unable to determine userId from request call",
                status = HttpStatusCode.InternalServerError
            )
            return@on
        }

        val request = AuthorizationRequest(
            userId = userId,
            routeResource = pluginConfig.resource(call),
            routeResourceId = pluginConfig.resourceId(call),
            resourceAction = pluginConfig.action(call),
        )

        val isAuthorizedResult = authorizationProvider.checkAuthorization(request)

        if (isAuthorizedResult.isFailure) {
            call.respondText("Unable to access authorization service", status = HttpStatusCode.InternalServerError)
        }

        val isAuthorized = isAuthorizedResult.getOrThrow()
        val logContext =
            "user=${request.userId}, action=${request.resourceAction}, resourceType=${request.routeResource}, resourceId=${request.routeResourceId}"

        if (!isAuthorized) {
            call.application.log.info("access denied: $logContext")
            call.respondText("Forbidden", status = HttpStatusCode.Forbidden)
            return@on
        }

        call.application.log.info("access granted: $logContext")
    }
}

public class AuthorizationRouteSelector : RouteSelector() {
    override suspend fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation.Transparent
    }
}

fun Route.authorize(
    authorizationRouteConfig: AuthorizationRoutePluginConfiguration.() -> Unit,
    build: Route.() -> Unit
) {
    createChild(AuthorizationRouteSelector())
        .apply { install(AuthorizationRoute, authorizationRouteConfig) }
        .apply { build() }
}

fun Route.authorize(
    authorizationRouteConfig: AuthorizationRoutePluginConfiguration.() -> Unit,
) {
    install(AuthorizationRoute, authorizationRouteConfig)
}
