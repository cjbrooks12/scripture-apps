package com.caseyjbrooks.app

import android.app.Application
import android.content.Context
import com.caseyjbrooks.di.GlobalKoinApplication
import com.caseyjbrooks.di.Variant
import com.caseyjbrooks.di.getModulesForVariant
import com.caseyjbrooks.domain.bus.EventBusService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.dsl.binds
import org.koin.dsl.module

class MainAndroidApplication : Application() {

    private val applicationCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
//        FirebaseApp.initializeApp(this)
        val variant = Variant(Variant.Environment.Local, Variant.BuildType.Debug)

        GlobalKoinApplication.init {
            modules(AbideApplicationKoinModule(applicationCoroutineScope).getModulesForVariant(variant))
            modules(
                module {
                    single { this@MainAndroidApplication }.binds(arrayOf(Context::class, Application::class))
                }
            )
        }

        applicationCoroutineScope.launch {
            val eventBusService: EventBusService = GlobalKoinApplication.get()
            val applicationStructure: AbideApplicationStructure = GlobalKoinApplication.get()

            with(eventBusService) {
                startSubscriptions(applicationStructure.eventBusSubscriptions)
            }
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationCoroutineScope.cancel()
    }
}
