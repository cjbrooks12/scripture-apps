package com.caseyjbrooks.gradebook.queues

import org.koin.dsl.module
import org.koin.ktor.plugin.RequestScope

val queuesKoinModule = module {
    scope<RequestScope> {

    }
}
