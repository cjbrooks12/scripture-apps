package com.copperleaf.biblebits.platform.configuration

import com.copperleaf.biblebits.platform.services.authorization.oauth.authorizeOAuth
import com.copperleaf.biblebits.platform.services.authorization.openfga.authorizeOpenFga
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.IgnoreTrailingSlash
import io.ktor.server.routing.Route
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.koin.core.KoinApplication

fun Application.configureRouting(
    apiVersion: Int,
    koinApplication: KoinApplication,
    publicRoutes: Route.() -> Unit,
    protectedRoutes: Route.() -> Unit,
    secureRoutes: Route.() -> Unit,
    adminRoutes: Route.() -> Unit,
) {
    install(IgnoreTrailingSlash)
    install(AutoHeadResponse)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    routing {
        configureCors(koinApplication)

        // public routes may be accessed without logging in. However, logged in users may also access these accounts, and
        // their account credentials will be available to the endpoint for logging purposes
        route("/api/v$apiVersion/public") {
            publicRoutes()
        }

        authenticateRoutes(required = true) {
            route("/api/v$apiVersion/protected") {
                authorizeOAuth("api:protected") {
                    authorizeOpenFga("can_access_protected", "service:api") {
                        protectedRoutes()
                    }
                }
            }

            route("/api/v$apiVersion/secure") {
                authorizeOAuth("api:secure") {
                    authorizeOpenFga("can_access_secure", "service:api") {
                        secureRoutes()
                    }
                }
            }

            route("/api/v$apiVersion/admin") {
                authorizeOAuth("api:admin") {
                    authorizeOpenFga("can_access_admin", "service:api") {
                        adminRoutes()
                    }
                }
            }
        }
    }
}

private fun Route.authorizeOpenFga(
    action: String,
    resource: String,
    build: Route.() -> Unit
) {
    authorizeOpenFga(
        routePluginName = "$action $resource",
        {
            this.action = { action }
            this.resource = { resource.split(":")[0] }
            this.resourceId = { resource.split(":")[1] }
        }, build
    )
}

private fun Route.authorizeOAuth(
    scope: String,
    build: Route.() -> Unit
) {
    authorizeOAuth(
        routePluginName = scope,
        authorizationRouteConfig = {
            this.providerName = null
            this.scope = setOf(scope)
        },
        build = build,
    )
}
