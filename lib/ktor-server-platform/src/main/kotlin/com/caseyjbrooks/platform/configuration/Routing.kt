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
    koinApplication: KoinApplication,
    publicAccessRoutes: Route.() -> Unit,
    authenticatedRoutes: Route.() -> Unit,
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
        route("/api/v1/public") {
            authenticateRoutes(required = false) {
                publicAccessRoutes()
            }
        }

        // public routes may only be accessed by a logged-in user. Fine-grained authorization checks will be applied to
        // every route in addition to basic authentication.
        route("/api/v1/protected") {
            authenticateRoutes(required = true) {
                authenticatedRoutes()
            }
        }
    }
}
