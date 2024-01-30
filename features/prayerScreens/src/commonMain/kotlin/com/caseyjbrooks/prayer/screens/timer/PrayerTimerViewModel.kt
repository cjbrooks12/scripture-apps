package com.caseyjbrooks.prayer.screens.timer

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class PrayerTimerViewModel(
    config: BallastViewModelConfiguration<
            PrayerTimerContract.Inputs,
            PrayerTimerContract.Events,
            PrayerTimerContract.State>,
    eventHandler: EventHandler<
            PrayerTimerContract.Inputs,
            PrayerTimerContract.Events,
            PrayerTimerContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        PrayerTimerContract.Inputs,
        PrayerTimerContract.Events,
        PrayerTimerContract.State>(
    config, eventHandler, coroutineScope
)
