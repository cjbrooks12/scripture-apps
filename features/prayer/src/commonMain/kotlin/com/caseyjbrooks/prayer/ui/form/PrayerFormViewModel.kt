package com.caseyjbrooks.prayer.ui.form

import com.copperleaf.ballast.BallastViewModel

internal typealias PrayerFormViewModel = BallastViewModel<
        PrayerFormContract.Inputs,
        PrayerFormContract.Events,
        PrayerFormContract.State,
        >
