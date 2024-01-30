package com.caseyjbrooks.foryou.ui.dashboard

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class ForYouDashboardEventHandler : EventHandler<
        ForYouDashboardContract.Inputs,
        ForYouDashboardContract.Events,
        ForYouDashboardContract.State> {
    override suspend fun EventHandlerScope<
            ForYouDashboardContract.Inputs,
            ForYouDashboardContract.Events,
            ForYouDashboardContract.State>.handleEvent(
        event: ForYouDashboardContract.Events
    ) {
    }
}
