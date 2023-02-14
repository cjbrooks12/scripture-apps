package com.caseyjbrooks.scripturenow.viewmodel.memory.detail

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

public class MemoryVerseDetailsEventHandler(
    private val router: Router<ScriptureNowRoute>,
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
        is MemoryVerseDetailsContract.Events.NavigateTo -> {
            router.send(RouterContract.Inputs.GoToDestination(event.destination))
        }

        is MemoryVerseDetailsContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
