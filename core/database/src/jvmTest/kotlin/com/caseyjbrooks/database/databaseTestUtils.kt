package com.caseyjbrooks.database

import org.koin.core.Koin
import org.koin.dsl.koinApplication

inline fun koinTest(
    block: Koin.() -> Unit
) {
    val koinApp = koinApplication {
        modules(
            fakeDatabaseModule,
            jvmDatabaseModule,
        )
    }
    try {
        block(koinApp.koin)
    } finally {
        koinApp.close()
    }
}
