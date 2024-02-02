package com.caseyjbrooks.app

import android.app.Application
import android.content.Context
import com.caseyjbrooks.di.GlobalKoinApplication
import com.caseyjbrooks.routing.ApplicationStructure
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
        GlobalKoinApplication.koinApplication = koinApplication {
            coroutinesEngine()

            modules(
                AbideApplicationStructure.koinModule,
                module {
                    single { this@MainAndroidApplication }.binds(arrayOf(Context::class, Application::class))
                    single<CoroutineScope> { appCoroutineScope }
                    singleOf(::AbideApplicationStructure).bind<ApplicationStructure>()
                }
            )
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        appCoroutineScope.cancel()
    }
}
