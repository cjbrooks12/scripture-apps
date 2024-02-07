package com.caseyjbrooks.app

import android.app.Application
import android.content.Context
import com.caseyjbrooks.di.GlobalKoinApplication
import com.caseyjbrooks.di.Variant
import com.caseyjbrooks.di.getModulesForVariant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.extension.coroutinesEngine
import org.koin.dsl.binds
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class MainAndroidApplication : Application() {

    private val applicationCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate() {
        super.onCreate()
//        FirebaseApp.initializeApp(this)
        val variant = Variant(Variant.Environment.Local, Variant.BuildType.Debug)

        GlobalKoinApplication.koinApplication = koinApplication {
            coroutinesEngine()
            modules(AbideApplicationKoinModule(applicationCoroutineScope).getModulesForVariant(variant))
            modules(
                module {
                    single { this@MainAndroidApplication }.binds(arrayOf(Context::class, Application::class))
                }
            )
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationCoroutineScope.cancel()
    }
}
