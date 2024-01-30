package com.caseyjbrooks.prayer.screens.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class PrayerListViewModel(
    config: BallastViewModelConfiguration<
            PrayerListContract.Inputs,
            PrayerListContract.Events,
            PrayerListContract.State>,
    eventHandler: EventHandler<
            PrayerListContract.Inputs,
            PrayerListContract.Events,
            PrayerListContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State>(
    config, eventHandler, coroutineScope
)
