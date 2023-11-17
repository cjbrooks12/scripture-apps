package com.caseyjbrooks.prayer.ui.timer

import com.copperleaf.ballast.BallastViewModel

internal typealias PrayerTimerViewModel = BallastViewModel<
        PrayerTimerContract.Inputs,
        PrayerTimerContract.Events,
        PrayerTimerContract.State,
        >
