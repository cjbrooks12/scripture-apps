package com.copperleaf.beer.main

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

class MainEventHandler : EventHandler<
    MainContract.Inputs,
    MainContract.Events,
    MainContract.State> {
    override suspend fun EventHandlerScope<
        MainContract.Inputs,
        MainContract.Events,
        MainContract.State>.handleEvent(
        event: MainContract.Events
    ) {}
}
