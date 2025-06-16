package com.copperleaf.biblebits.routing

import com.caseyjbrooks.platform.util.GET
import com.copperleaf.biblebits.controller.DebugController
import io.ktor.server.routing.Route

// Router
// ---------------------------------------------------------------------------------------------------------------------

fun Route.debugRouter() {
    GET("/server-info") {
        DebugController.Companion(call).serverInfo()
    }
    GET("/headers") {
        DebugController.Companion(call).headers(
            headers = call.request.headers,
        )
    }
    GET("/me") {
        DebugController.Companion(call).oidcCurrentUserInfo(
            headers = call.request.headers,
        )
    }
    GET("/db-test") {
        DebugController.Companion(call).databaseTest()
    }
    GET("/cache-test") {
        DebugController.Companion(call).cacheTest()
    }
}


