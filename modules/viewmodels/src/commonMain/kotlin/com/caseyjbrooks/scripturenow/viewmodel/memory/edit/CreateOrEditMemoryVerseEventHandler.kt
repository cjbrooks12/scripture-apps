package com.caseyjbrooks.scripturenow.viewmodel.memory.edit

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

public class CreateOrEditMemoryVerseEventHandler(
    private val router: Router<ScriptureNowRoute>,
) : EventHandler<
        CreateOrEditMemoryVerseContract.Inputs,
        CreateOrEditMemoryVerseContract.Events,
        CreateOrEditMemoryVerseContract.State> {
    override suspend fun EventHandlerScope<
            CreateOrEditMemoryVerseContract.Inputs,
            CreateOrEditMemoryVerseContract.Events,
            CreateOrEditMemoryVerseContract.State>.handleEvent(
        event: CreateOrEditMemoryVerseContract.Events
    ): Unit = when (event) {
        is CreateOrEditMemoryVerseContract.Events.NavigateTo -> {
            router.send(RouterContract.Inputs.GoToDestination(event.destination))
        }

        is CreateOrEditMemoryVerseContract.Events.NavigateUp -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
