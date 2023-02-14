package com.caseyjbrooks.scripturenow.viewmodel.memory.list

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

public class MemoryVerseListEventHandler(
    private val router: Router<ScriptureNowRoute>
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
            router.send(RouterContract.Inputs.GoToDestination(event.destination))
        }

        MemoryVerseListContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
