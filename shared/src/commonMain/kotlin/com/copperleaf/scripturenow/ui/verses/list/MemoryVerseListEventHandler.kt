package com.copperleaf.scripturenow.ui.verses.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.scripturenow.ui.router.MainRouterViewModel

class MemoryVerseListEventHandler(
    private val routerViewModel: MainRouterViewModel
) : EventHandler<
    MemoryVerseListContract.Inputs,
    MemoryVerseListContract.Events,
    MemoryVerseListContract.State> {
    override suspend fun EventHandlerScope<
        MemoryVerseListContract.Inputs,
        MemoryVerseListContract.Events,
        MemoryVerseListContract.State>.handleEvent(
        event: MemoryVerseListContract.Events
    ) = when (event) {
        is MemoryVerseListContract.Events.NavigateTo -> {
            routerViewModel.send(RouterContract.Inputs.GoToDestination(event.destination))
        }
        MemoryVerseListContract.Events.NavigateBack -> {
            routerViewModel.send(RouterContract.Inputs.GoBack)
        }
    }
}
