package com.copperleaf.scripturenow.ui.verses.edit

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.scripturenow.repositories.router.MainRouterViewModel

class CreateOrEditMemoryVerseEventHandler(
    private val routerViewModel: MainRouterViewModel
) : EventHandler<
    CreateOrEditMemoryVerseContract.Inputs,
    CreateOrEditMemoryVerseContract.Events,
    CreateOrEditMemoryVerseContract.State> {
    override suspend fun EventHandlerScope<
        CreateOrEditMemoryVerseContract.Inputs,
        CreateOrEditMemoryVerseContract.Events,
        CreateOrEditMemoryVerseContract.State>.handleEvent(
        event: CreateOrEditMemoryVerseContract.Events
    ) = when (event) {
        is CreateOrEditMemoryVerseContract.Events.NavigateUp -> {
            routerViewModel.send(RouterContract.Inputs.GoBack)
        }
    }
}
