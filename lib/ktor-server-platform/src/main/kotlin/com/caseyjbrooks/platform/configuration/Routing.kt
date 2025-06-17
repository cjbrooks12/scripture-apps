package com.caseyjbrooks.platform.configuration

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

        route("/api/v$apiVersion/protected") {
            authenticateRoutes(name = "protected", required = true) {
                protectedRoutes()
            }
        }

        route("/api/v$apiVersion/secure") {
            authenticateRoutes(name = "secure", required = true) {
                secureRoutes()
            }
        }

        route("/api/v$apiVersion/admin") {
            authenticateRoutes(name = "admin", required = true) {
                adminRoutes()
            }
        }
    }
}
