package com.copperleaf.scripturenow.ui.votd

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.router.RouterContract
import com.copperleaf.scripturenow.ui.router.MainRouterViewModel

class VotdEventHandler(
    private val routerViewModel: MainRouterViewModel,
) : EventHandler<
    VotdContract.Inputs,
    VotdContract.Events,
    VotdContract.State> {
    override suspend fun EventHandlerScope<
        VotdContract.Inputs,
        VotdContract.Events,
        VotdContract.State>.handleEvent(
        event: VotdContract.Events
    ): Unit = when (event) {
        is VotdContract.Events.NavigateUp -> {
            routerViewModel.send(RouterContract.Inputs.GoBack)
        }
    }
}
