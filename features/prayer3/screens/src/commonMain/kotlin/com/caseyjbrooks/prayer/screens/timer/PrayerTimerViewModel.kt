package com.caseyjbrooks.prayer.screens.timer

import com.copperleaf.ballast.BallastViewModel

internal typealias PrayerTimerViewModel = BallastViewModel<
        PrayerTimerContract.Inputs,
        PrayerTimerContract.Events,
        PrayerTimerContract.State,
        >
