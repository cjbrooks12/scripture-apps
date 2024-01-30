package com.caseyjbrooks.prayer.screens.form

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class PrayerFormViewModel(
    config: BallastViewModelConfiguration<
            PrayerFormContract.Inputs,
            PrayerFormContract.Events,
            PrayerFormContract.State>,
    eventHandler: EventHandler<
            PrayerFormContract.Inputs,
            PrayerFormContract.Events,
            PrayerFormContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        PrayerFormContract.Inputs,
        PrayerFormContract.Events,
        PrayerFormContract.State>(
    config, eventHandler, coroutineScope
)
