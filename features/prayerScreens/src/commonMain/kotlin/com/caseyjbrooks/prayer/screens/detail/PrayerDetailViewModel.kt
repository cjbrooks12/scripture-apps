package com.caseyjbrooks.prayer.screens.detail

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class PrayerDetailViewModel(
    config: BallastViewModelConfiguration<
            PrayerDetailContract.Inputs,
            PrayerDetailContract.Events,
            PrayerDetailContract.State>,
    eventHandler: EventHandler<
            PrayerDetailContract.Inputs,
            PrayerDetailContract.Events,
            PrayerDetailContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        PrayerDetailContract.Inputs,
        PrayerDetailContract.Events,
        PrayerDetailContract.State>(
    config, eventHandler, coroutineScope
)
