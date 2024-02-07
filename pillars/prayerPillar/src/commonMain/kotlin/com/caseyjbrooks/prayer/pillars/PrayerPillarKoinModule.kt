package com.caseyjbrooks.prayer.pillars

import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.prayer.PrayerDataKoinModule
import com.caseyjbrooks.prayer.domain.PrayerDomainKoinModule
import com.caseyjbrooks.prayer.schedules.PrayerSchedulesKoinModule
import com.caseyjbrooks.prayer.screens.PrayerScreensKoinModule

public class PrayerPillarKoinModule : KoinModule {
    override fun relatedModules(): List<KoinModule> = listOf(
        PrayerDataKoinModule(),
        PrayerDomainKoinModule(),
        PrayerScreensKoinModule(),
        PrayerSchedulesKoinModule(),
    )
}
