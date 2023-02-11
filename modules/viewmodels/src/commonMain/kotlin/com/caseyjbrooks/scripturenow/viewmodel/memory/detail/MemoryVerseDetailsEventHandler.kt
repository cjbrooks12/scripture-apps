package com.caseyjbrooks.scripturenow.viewmodel.memory.detail

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

public class MemoryVerseDetailsEventHandler(
//    private val routerViewModel: MainRouterViewModel
) : EventHandler<
        MemoryVerseDetailsContract.Inputs,
        MemoryVerseDetailsContract.Events,
        MemoryVerseDetailsContract.State> {
    override suspend fun EventHandlerScope<
            MemoryVerseDetailsContract.Inputs,
            MemoryVerseDetailsContract.Events,
            MemoryVerseDetailsContract.State>.handleEvent(
        event: MemoryVerseDetailsContract.Events
    ): Unit = when (event) {
        is MemoryVerseDetailsContract.Events.NavigateBack -> {
//            routerViewModel.send(RouterContract.Inputs.GoBack)
        }

        is MemoryVerseDetailsContract.Events.NavigateTo -> {
//            routerViewModel.send(RouterContract.Inputs.GoToDestination(event.destination))
        }
    }
}
