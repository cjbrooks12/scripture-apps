package com.caseyjbrooks.gradebook.routing

import com.caseyjbrooks.gradebook.controller.DebugController
import com.caseyjbrooks.platform.util.GET
import io.ktor.server.routing.Route

// Router
// ---------------------------------------------------------------------------------------------------------------------

fun Route.debugRouter() {
    GET("/server-info") {
        DebugController(call).serverInfo()
    }
    GET("/headers") {
        DebugController(call).headers(
            headers = call.request.headers,
        )
    }
    GET("/me") {
        DebugController(call).oidcCurrentUserInfo(
            headers = call.request.headers,
        )
    }
    GET("/db-test") {
        DebugController(call).databaseTest()
    }
    GET("/cache-test") {
        DebugController(call).cacheTest()
    }
}


