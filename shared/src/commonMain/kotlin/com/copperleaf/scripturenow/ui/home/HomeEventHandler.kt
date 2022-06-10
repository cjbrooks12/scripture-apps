package com.copperleaf.scripturenow.ui.home

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

class HomeEventHandler : EventHandler<
    HomeContract.Inputs,
    HomeContract.Events,
    HomeContract.State> {
    override suspend fun EventHandlerScope<
        HomeContract.Inputs,
        HomeContract.Events,
        HomeContract.State>.handleEvent(
        event: HomeContract.Events
    ) = when (event) {
        is HomeContract.Events.NavigateUp -> {

        }
    }
}
