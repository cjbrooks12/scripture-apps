package com.copperleaf.biblebits.platform.util.routing

import io.ktor.server.application.ApplicationCall
import io.ktor.server.routing.RoutingCall

object ApplicationCallExtractor : Extractor<ApplicationCall> {
    override fun invoke(scope: ExtractorScope): ApplicationCall {
        return scope.call
    }
}

object RoutingCallExtractor : Extractor<RoutingCall> {
    override fun invoke(scope: ExtractorScope): RoutingCall {
        return scope.call
    }
}

