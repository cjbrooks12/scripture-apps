package com.copperleaf.biblebits.routing

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import org.koin.dsl.module
import org.koin.ktor.plugin.RequestScope

val routingKoinModule = module {
    scope<RequestScope> {
        scoped<JWTPrincipal> {
            get<ApplicationCall>().principal<JWTPrincipal>()!!
        }
    }
}
