package com.caseyjbrooks.scripturenow.viewmodel.memory.edit

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

public class CreateOrEditMemoryVerseEventHandler(
//    private val routerViewModel: MainRouterViewModel
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
        is CreateOrEditMemoryVerseContract.Events.NavigateUp -> {
//            routerViewModel.send(RouterContract.Inputs.GoBack)
        }
    }
}
