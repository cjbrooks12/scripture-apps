package com.caseyjbrooks.app

import android.app.Application
import android.content.Context
import com.caseyjbrooks.database.androidDatabaseModule
import com.caseyjbrooks.di.ApplicationStructure
import com.caseyjbrooks.di.GlobalScriptureNowKoinApplication
import com.caseyjbrooks.logging.loggingModule
import com.caseyjbrooks.ui.androidNotificationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.extension.coroutinesEngine
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class MainAndroidApplication : Application() {

    private val appCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate() {
        super.onCreate()
//        FirebaseApp.initializeApp(this)
        GlobalScriptureNowKoinApplication.koinApplication = koinApplication {
            coroutinesEngine()

            modules(
                androidDatabaseModule,
                androidNotificationModule,
                loggingModule,
                realScriptureNowAppModule,
                module {
                    single { this@MainAndroidApplication }.binds(arrayOf(Context::class, Application::class))
                    single<CoroutineScope> { appCoroutineScope }
                    singleOf(::ScriptureNowApplicationStructure).bind<ApplicationStructure>()
                }
            )
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        appCoroutineScope.cancel()
    }
}
