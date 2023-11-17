package com.caseyjbrooks.prayer.ui.list

import com.copperleaf.ballast.BallastViewModel

internal typealias PrayerListViewModel = BallastViewModel<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State,
        >
