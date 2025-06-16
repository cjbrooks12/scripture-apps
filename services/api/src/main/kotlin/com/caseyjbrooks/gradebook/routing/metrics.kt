package com.caseyjbrooks.gradebook.routing

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.koin.ktor.plugin.scope

fun Route.metricsRouter() {
    get("/metrics") {
        val appMicrometerRegistry = call.scope.get<PrometheusMeterRegistry>()
        call.respond(appMicrometerRegistry.scrape())
    }
}

