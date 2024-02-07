package com.caseyjbrooks.prayer.schedules

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.di.KoinModule
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public class PrayerSchedulesKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::PrayerSchedulesInputHandler)
        factoryOf(::PrayerSchedulesEventHandler)

        single<PrayerSchedulesViewModel> {
            val applicationCoroutineScope: CoroutineScope = get()
            PrayerSchedulesViewModel(
                coroutineScope = applicationCoroutineScope,
                config = buildWithViewModel(
                    initialState = PrayerSchedulesContract.State(),
                    inputHandler = get<PrayerSchedulesInputHandler>(),
                    name = "Prayer Schedules",
                ),
                eventHandler = get<PrayerSchedulesEventHandler>(),
            )
        }
    }
}
