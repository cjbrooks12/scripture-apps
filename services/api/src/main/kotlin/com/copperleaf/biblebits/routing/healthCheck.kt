package com.copperleaf.biblebits.routing

import com.copperleaf.biblebits.controller.HealthCheckController
import com.copperleaf.biblebits.platform.util.GET
import io.ktor.server.routing.Route

fun Route.healthCheckRouter() {
    GET("/health") {
        HealthCheckController.Companion(call).health()
    }
}

