package com.caseyjbrooks.app.desktop

import androidx.compose.ui.window.singleWindowApplication
import com.caseyjbrooks.app.ScriptureNowApplicationStructure
import com.caseyjbrooks.app.commonApplicationModule
import com.caseyjbrooks.app.realScriptureNowAppModule
import com.caseyjbrooks.di.GlobalScriptureNowKoinApplication
import com.caseyjbrooks.routing.ApplicationStructure
import com.caseyjbrooks.ui.ApplicationRoot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.extension.coroutinesEngine
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.koinApplication
import org.koin.dsl.module

public fun main() {
    val appCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    GlobalScriptureNowKoinApplication.koinApplication = koinApplication {
        coroutinesEngine()

        modules(
            commonApplicationModule,
            realScriptureNowAppModule,
            module {
                single<CoroutineScope> { appCoroutineScope }
                singleOf(::ScriptureNowApplicationStructure).bind<ApplicationStructure>()
            }
        )
    }

    singleWindowApplication {
        ApplicationRoot(GlobalScriptureNowKoinApplication.koinApplication.koin, null)
    }
}
