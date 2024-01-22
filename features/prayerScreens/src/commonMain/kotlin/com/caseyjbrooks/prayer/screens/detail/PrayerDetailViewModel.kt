package com.caseyjbrooks.prayer.screens.detail

import com.copperleaf.ballast.BallastViewModel

internal typealias PrayerDetailViewModel = BallastViewModel<
        PrayerDetailContract.Inputs,
        PrayerDetailContract.Events,
        PrayerDetailContract.State,
        >
