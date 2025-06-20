package com.copperleaf.biblebits.platform.services.authorization.oauth

import com.copperleaf.biblebits.platform.services.authorization.Authorization
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.createRouteScopedPlugin
import io.ktor.server.application.log
import io.ktor.server.application.pluginOrNull
import io.ktor.server.auth.AuthenticationChecked
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.RouteSelector
import io.ktor.server.routing.RouteSelectorEvaluation
import io.ktor.server.routing.RoutingResolveContext

// Route-Scoped Plugin, which depends on the top-level plugin
// ---------------------------------------------------------------------------------------------------------------------

class OAuthRouteAuthorizationPluginConfiguration {
    var providerName: String? = null
    var scope: Set<String> = emptySet()
}

fun authorizationRoutePlugin(
    routePluginName: String,
) = createRouteScopedPlugin(
    name = routePluginName,
    createConfiguration = ::OAuthRouteAuthorizationPluginConfiguration
) {
    on(AuthenticationChecked) { call ->
        val topLevelAauthorizationPlugin = call
            .application
            .pluginOrNull(Authorization)
        if (topLevelAauthorizationPlugin == null) {
            call.respondText(
                "Authorization plugin not configured",
                status = HttpStatusCode.InternalServerError,
            )
            return@on
        }

        val topLevelAuthorizationPluginProvider = topLevelAauthorizationPlugin
            .config
            .getProvider<OAuthAuthorizationProvider>(call, pluginConfig.providerName)
        if (topLevelAuthorizationPluginProvider == null) {
            call.respondText(
                "OAuth Authorization named `${pluginConfig.providerName}` not configured",
                status = HttpStatusCode.InternalServerError,
            )
            return@on
        }

        val jwtScopes = topLevelAuthorizationPluginProvider.jwtScopes(call)
        if (jwtScopes == null) {
            call.respondText(
                "Unable to determine JWT scopes from request call",
                status = HttpStatusCode.InternalServerError
            )
            return@on
        }

        val isAuthorized = jwtScopes.containsAll(pluginConfig.scope)
        val logContext = "requested scope: ${pluginConfig.scope}"

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

fun Route.authorizeOAuth(
    routePluginName: String,
    authorizationRouteConfig: OAuthRouteAuthorizationPluginConfiguration.() -> Unit,
    build: Route.() -> Unit
) {
    createChild(AuthorizationRouteSelector())
        .apply { install(authorizationRoutePlugin(routePluginName), authorizationRouteConfig) }
        .apply { build() }
}
