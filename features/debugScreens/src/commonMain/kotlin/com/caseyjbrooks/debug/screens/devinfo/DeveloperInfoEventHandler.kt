package com.caseyjbrooks.debug.screens.devinfo

import com.caseyjbrooks.routing.RouterViewModel
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract

internal class DeveloperInfoEventHandler(
    private val router: RouterViewModel,
) : EventHandler<
        DeveloperInfoContract.Inputs,
        DeveloperInfoContract.Events,
        DeveloperInfoContract.State> {
    override suspend fun EventHandlerScope<
            DeveloperInfoContract.Inputs,
            DeveloperInfoContract.Events,
            DeveloperInfoContract.State>.handleEvent(
        event: DeveloperInfoContract.Events
    ): Unit = when (event) {
        is DeveloperInfoContract.Events.NavigateUp -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
