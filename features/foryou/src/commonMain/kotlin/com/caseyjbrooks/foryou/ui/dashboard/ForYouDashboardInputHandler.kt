package com.caseyjbrooks.foryou.ui.dashboard

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import kotlinx.coroutines.delay

internal class ForYouDashboardInputHandler : InputHandler<
        ForYouDashboardContract.Inputs,
        ForYouDashboardContract.Events,
        ForYouDashboardContract.State> {
    override suspend fun InputHandlerScope<
            ForYouDashboardContract.Inputs,
            ForYouDashboardContract.Events,
            ForYouDashboardContract.State>.handleInput(
        input: ForYouDashboardContract.Inputs
    ): Unit = when (input) {
        is ForYouDashboardContract.Inputs.Initialize -> {
            updateState { it.copy(loading = true) }
            delay(1000)
            updateState { it.copy(loading = false) }
        }

        is ForYouDashboardContract.Inputs.GoBack -> {
            postEvent(ForYouDashboardContract.Events.NavigateUp)
        }
    }
}
