package com.caseyjbrooks.prayer.schedules

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class PrayerSchedulesViewModel(
    config: BallastViewModelConfiguration<
            PrayerSchedulesContract.Inputs,
            PrayerSchedulesContract.Events,
            PrayerSchedulesContract.State>,
    eventHandler: EventHandler<
            PrayerSchedulesContract.Inputs,
            PrayerSchedulesContract.Events,
            PrayerSchedulesContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        PrayerSchedulesContract.Inputs,
        PrayerSchedulesContract.Events,
        PrayerSchedulesContract.State>(
    config, eventHandler, coroutineScope
)
