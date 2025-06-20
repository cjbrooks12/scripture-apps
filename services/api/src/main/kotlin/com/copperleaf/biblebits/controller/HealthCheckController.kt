package com.copperleaf.biblebits.controller

import com.caseyjbrooks.dto.HealthCheckResponse
import com.copperleaf.biblebits.platform.util.AppResponse
import com.copperleaf.biblebits.platform.util.ok
import io.ktor.server.application.ApplicationCall
import org.koin.core.parameter.parametersOf
import org.koin.ktor.plugin.scope

class HealthCheckController {
    public suspend fun health(): AppResponse.Json<HealthCheckResponse> {
        return ok(HealthCheckResponse(true))
    }

    companion object {
        operator fun invoke(call: ApplicationCall): HealthCheckController {
            return call.scope.get<HealthCheckController> { parametersOf(call) }
        }
    }
}
