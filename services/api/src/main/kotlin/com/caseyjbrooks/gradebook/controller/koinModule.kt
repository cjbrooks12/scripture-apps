package com.caseyjbrooks.gradebook.controller

import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import org.koin.ktor.plugin.RequestScope

val controllersKoinModule = module {
    scope<RequestScope> {
        scopedOf(::AccessController)

        scopedOf(::DebugController)
        scopedOf(::HealthCheckController)
    }
}
