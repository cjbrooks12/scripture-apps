package com.caseyjbrooks.prayer.screens

import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.prayer.screens.detail.PrayerDetailKoinModule
import com.caseyjbrooks.prayer.screens.form.PrayerFormKoinModule
import com.caseyjbrooks.prayer.screens.list.PrayerListKoinModule
import com.caseyjbrooks.prayer.screens.timer.PrayerTimerKoinModule

public class PrayerScreensKoinModule : KoinModule {
    override fun relatedModules(): List<KoinModule> = listOf(
        PrayerDetailKoinModule(),
        PrayerFormKoinModule(),
        PrayerListKoinModule(),
        PrayerTimerKoinModule(),
    )
}
