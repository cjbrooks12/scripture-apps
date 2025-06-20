package com.copperleaf.biblebits.platform.services.authorization.openfga

import com.copperleaf.biblebits.platform.services.authorization.Authorization
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
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

class OpenFgaRouteAuthorizationPluginConfiguration {
    var providerName: String? = null
    lateinit var action: suspend ApplicationCall.() -> String
    lateinit var resource: suspend ApplicationCall.() -> String
    lateinit var resourceId: suspend ApplicationCall.() -> String
}

data class OpenFgaAuthorizationRequest(
    val userId: String,
    val routeResource: String,
    val routeResourceId: String,
    val resourceAction: String,
)

private fun authorizationRoutePlugin(
    routePluginName: String,
) = createRouteScopedPlugin(
    name = routePluginName,
    createConfiguration = ::OpenFgaRouteAuthorizationPluginConfiguration
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
            .getProvider<OpenFgaAuthorizationProvider>(call, pluginConfig.providerName)
        if (topLevelAuthorizationPluginProvider == null) {
            call.respondText(
                "OpenFGA Authorization named `${pluginConfig.providerName}` not configured",
                status = HttpStatusCode.InternalServerError,
            )
            return@on
        }

        val userId = topLevelAuthorizationPluginProvider.userId(call)
        if (userId == null) {
            call.respondText(
                "Unable to determine userId from request call",
                status = HttpStatusCode.InternalServerError
            )
            return@on
        }

        val request = OpenFgaAuthorizationRequest(
            userId = userId,
            routeResource = pluginConfig.resource(call),
            routeResourceId = pluginConfig.resourceId(call),
            resourceAction = pluginConfig.action(call),
        )

        val isAuthorizedResult = topLevelAuthorizationPluginProvider.checkAuthorization(request)

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

fun Route.authorizeOpenFga(
    routePluginName: String,
    authorizationRouteConfig: OpenFgaRouteAuthorizationPluginConfiguration.() -> Unit,
    build: Route.() -> Unit
) {
    createChild(AuthorizationRouteSelector())
        .apply { install(authorizationRoutePlugin(routePluginName), authorizationRouteConfig) }
        .apply { build() }
}
