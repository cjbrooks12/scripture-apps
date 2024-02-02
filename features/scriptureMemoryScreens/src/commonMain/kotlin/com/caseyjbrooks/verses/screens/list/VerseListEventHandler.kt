package com.caseyjbrooks.verses.screens.list

import com.caseyjbrooks.routing.RouterViewModel
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract

internal class VerseListEventHandler(
    private val router: RouterViewModel,
) : EventHandler<
        VerseListContract.Inputs,
        VerseListContract.Events,
        VerseListContract.State,
        > {
    override suspend fun EventHandlerScope<
            VerseListContract.Inputs,
            VerseListContract.Events,
            VerseListContract.State,
            >.handleEvent(
        event: VerseListContract.Events,
    ): Unit = when (event) {
        is VerseListContract.Events.NavigateTo -> {
            if (event.replaceTop) {
                router.send(RouterContract.Inputs.ReplaceTopDestination(event.destination))
            } else {
                router.send(RouterContract.Inputs.GoToDestination(event.destination))
            }
        }

        is VerseListContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
