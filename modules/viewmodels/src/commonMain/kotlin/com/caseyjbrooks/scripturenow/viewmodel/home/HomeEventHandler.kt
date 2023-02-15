package com.caseyjbrooks.scripturenow.viewmodel.home

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

public class HomeEventHandler(
    private val router: Router<ScriptureNowRoute>
) : EventHandler<
        HomeContract.Inputs,
        HomeContract.Events,
        HomeContract.State> {
    override suspend fun EventHandlerScope<
            HomeContract.Inputs,
            HomeContract.Events,
            HomeContract.State>.handleEvent(
        event: HomeContract.Events
    ): Unit = when (event) {
        is HomeContract.Events.NavigateTo -> {
            router.send(RouterContract.Inputs.GoToDestination(event.destination))
        }

        is HomeContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
