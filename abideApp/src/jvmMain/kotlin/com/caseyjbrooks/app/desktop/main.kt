package com.caseyjbrooks.app.desktop

import androidx.compose.ui.window.singleWindowApplication
import com.caseyjbrooks.app.AbideApplicationStructure
import com.caseyjbrooks.di.GlobalKoinApplication
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
    GlobalKoinApplication.koinApplication = koinApplication {
        coroutinesEngine()

        modules(
            AbideApplicationStructure.koinModule,
            module {
                single<CoroutineScope> { appCoroutineScope }
                singleOf(::AbideApplicationStructure).bind<ApplicationStructure>()
            }
        )
    }

    singleWindowApplication {
        ApplicationRoot(GlobalKoinApplication.koinApplication.koin, null)
    }
}
