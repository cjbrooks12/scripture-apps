package com.caseyjbrooks.scripturenow.viewmodel.memory.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

public class MemoryVerseListEventHandler(
//    private val routerViewModel: MainRouterViewModel
) : EventHandler<
        MemoryVerseListContract.Inputs,
        MemoryVerseListContract.Events,
        MemoryVerseListContract.State> {
    override suspend fun EventHandlerScope<
            MemoryVerseListContract.Inputs,
            MemoryVerseListContract.Events,
            MemoryVerseListContract.State>.handleEvent(
        event: MemoryVerseListContract.Events
    ): Unit = when (event) {
        is MemoryVerseListContract.Events.NavigateTo -> {
//            routerViewModel.send(RouterContract.Inputs.GoToDestination(event.destination))
        }

        MemoryVerseListContract.Events.NavigateBack -> {
//            routerViewModel.send(RouterContract.Inputs.GoBack)
        }
    }
}
