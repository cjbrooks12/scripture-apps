package com.caseyjbrooks.foryou.ui.dashboard

import com.copperleaf.ballast.BallastViewModel

internal typealias ForYouDashboardViewModel = BallastViewModel<
        ForYouDashboardContract.Inputs,
        ForYouDashboardContract.Events,
        ForYouDashboardContract.State>
