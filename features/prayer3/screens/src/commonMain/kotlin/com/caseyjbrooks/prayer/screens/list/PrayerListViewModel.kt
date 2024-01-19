package com.caseyjbrooks.prayer.screens.list

import com.copperleaf.ballast.BallastViewModel

internal typealias PrayerListViewModel = BallastViewModel<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State,
        >
