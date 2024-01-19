package com.caseyjbrooks.app

import android.app.Application
import android.content.Context
import com.caseyjbrooks.database.UuidFactory
import com.caseyjbrooks.database.UuidFactoryImpl
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.prayer.models.PrayerUser
import com.caseyjbrooks.shared.MainAppModule
import com.caseyjbrooks.shared.realAndroidApplicationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.datetime.Clock
import org.koin.core.KoinApplication
import org.koin.core.extension.coroutinesEngine
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class MainAndroidApplication : Application() {

    private val appCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
//        FirebaseApp.initializeApp(this)
        koinApplication = koinApplication {
            coroutinesEngine()

            modules(
                realAndroidApplicationModule,
                module {
                    single { this@MainAndroidApplication }.binds(arrayOf(Context::class, Application::class))
                    singleOf(::MainAppModule).bind(Pillar::class)
                    single<CoroutineScope> { appCoroutineScope }
                    single<PrayerUser?> { PrayerUser("Casey", PrayerUser.SubscriptionStatus.Free) }
                    single<UuidFactory> { UuidFactoryImpl() }
                    single<Clock> { Clock.System }
                }
            )
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        appCoroutineScope.cancel()
    }

    companion object {
        public lateinit var koinApplication: KoinApplication
    }
}
