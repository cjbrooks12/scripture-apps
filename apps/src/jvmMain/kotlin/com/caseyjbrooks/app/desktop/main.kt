package com.caseyjbrooks.app.desktop

import androidx.compose.ui.window.singleWindowApplication
import com.caseyjbrooks.app.AbideApplicationKoinModule
import com.caseyjbrooks.di.GlobalKoinApplication
import com.caseyjbrooks.di.Variant
import com.caseyjbrooks.di.getModulesForVariant
import com.caseyjbrooks.ui.ApplicationRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.extension.coroutinesEngine

public fun main() {
    val applicationCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    val variant = Variant(Variant.Environment.Local, Variant.BuildType.Debug)

    GlobalKoinApplication.init {
        coroutinesEngine()
        modules(AbideApplicationKoinModule(applicationCoroutineScope).getModulesForVariant(variant))
    }

    singleWindowApplication {
        ApplicationRoot(GlobalKoinApplication.koin, null)
    }
}
