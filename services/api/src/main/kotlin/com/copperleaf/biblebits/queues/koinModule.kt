package com.copperleaf.biblebits.queues

import org.koin.dsl.module
import org.koin.ktor.plugin.RequestScope

val queuesKoinModule = module {
    scope<RequestScope> {

    }
}
