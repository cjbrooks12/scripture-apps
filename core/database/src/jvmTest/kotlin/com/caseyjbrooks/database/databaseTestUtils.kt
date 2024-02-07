package com.caseyjbrooks.database

import com.caseyjbrooks.di.Variant
import com.caseyjbrooks.di.getModulesForVariant
import org.koin.core.Koin
import org.koin.dsl.koinApplication

inline fun koinTest(
    block: Koin.() -> Unit
) {
    val koinApp = koinApplication {
        val variant = Variant(Variant.Environment.Test, Variant.BuildType.Debug)
        modules(
            *DatabaseKoinModule().getModulesForVariant(variant).toTypedArray(),
        )
    }
    try {
        block(koinApp.koin)
    } finally {
        koinApp.close()
    }
}
