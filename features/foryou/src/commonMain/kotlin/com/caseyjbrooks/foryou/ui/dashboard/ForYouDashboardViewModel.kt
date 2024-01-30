package com.caseyjbrooks.foryou.ui.dashboard

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class ForYouDashboardViewModel(
    config: BallastViewModelConfiguration<
            ForYouDashboardContract.Inputs,
            ForYouDashboardContract.Events,
            ForYouDashboardContract.State>,
    eventHandler: EventHandler<
            ForYouDashboardContract.Inputs,
            ForYouDashboardContract.Events,
            ForYouDashboardContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        ForYouDashboardContract.Inputs,
        ForYouDashboardContract.Events,
        ForYouDashboardContract.State>(
    config, eventHandler, coroutineScope
)
