package com.copperleaf.scripturenow.ui.verses.detail

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.scripturenow.repositories.router.MainRouterViewModel

class MemoryVerseDetailsEventHandler(
    private val routerViewModel: MainRouterViewModel
) : EventHandler<
    MemoryVerseDetailsContract.Inputs,
    MemoryVerseDetailsContract.Events,
    MemoryVerseDetailsContract.State> {
    override suspend fun EventHandlerScope<
        MemoryVerseDetailsContract.Inputs,
        MemoryVerseDetailsContract.Events,
        MemoryVerseDetailsContract.State>.handleEvent(
        event: MemoryVerseDetailsContract.Events
    ) = when (event) {
        is MemoryVerseDetailsContract.Events.NavigateBack -> {
            routerViewModel.send(RouterContract.Inputs.GoBack)
        }
        is MemoryVerseDetailsContract.Events.NavigateTo -> {
            routerViewModel.send(RouterContract.Inputs.GoToDestination(event.destination))
        }
    }
}
