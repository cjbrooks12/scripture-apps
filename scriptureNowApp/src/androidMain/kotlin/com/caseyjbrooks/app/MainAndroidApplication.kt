package com.caseyjbrooks.app

import android.app.Application
import android.content.Context
import com.caseyjbrooks.bible.BibleModule
import com.caseyjbrooks.database.androidDatabaseModule
import com.caseyjbrooks.di.CombinedPillar
import com.caseyjbrooks.di.Pillar
import com.caseyjbrooks.foryou.ForYouModule
import com.caseyjbrooks.prayer.pillars.PrayerPillar
import com.caseyjbrooks.scripturememory.ScriptureMemoryModule
import com.caseyjbrooks.settings.SettingsModule
import com.caseyjbrooks.topicalbible.TopicalBibleModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.KoinApplication
import org.koin.core.extension.coroutinesEngine
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
                androidDatabaseModule,
                realScriptureNowAppModule,
                module {
                    single { this@MainAndroidApplication }.binds(arrayOf(Context::class, Application::class))
                    single<CoroutineScope> { appCoroutineScope }
                    single<Pillar> {
                        CombinedPillar(
                            ForYouModule(),
                            ScriptureMemoryModule(),
                            PrayerPillar(),
                            SettingsModule(),
                            TopicalBibleModule(),
                            BibleModule(),

                            initialRoute = ForYouModule().initialRoute,
                        )
                    }
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
