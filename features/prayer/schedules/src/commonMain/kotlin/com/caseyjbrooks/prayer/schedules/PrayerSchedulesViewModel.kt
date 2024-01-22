package com.caseyjbrooks.prayer.schedules

import com.copperleaf.ballast.BallastViewModel

internal typealias PrayerSchedulesViewModel = BallastViewModel<
        PrayerSchedulesContract.Inputs,
        PrayerSchedulesContract.Events,
        PrayerSchedulesContract.State,
        >
