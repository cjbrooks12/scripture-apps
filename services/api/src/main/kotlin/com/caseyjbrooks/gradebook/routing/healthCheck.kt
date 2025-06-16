package com.caseyjbrooks.gradebook.routing

import com.caseyjbrooks.gradebook.controller.HealthCheckController
import com.caseyjbrooks.platform.util.GET
import io.ktor.server.routing.Route

fun Route.healthCheckRouter() {
    GET("/health") {
        HealthCheckController(call).health()
    }
}

