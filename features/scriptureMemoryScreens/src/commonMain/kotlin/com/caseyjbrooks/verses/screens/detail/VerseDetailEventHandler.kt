package com.caseyjbrooks.verses.screens.detail

import com.caseyjbrooks.routing.RouterViewModel
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract

internal class VerseDetailEventHandler(
    private val router: RouterViewModel,
) : EventHandler<
        VerseDetailContract.Inputs,
        VerseDetailContract.Events,
        VerseDetailContract.State,
        > {
    override suspend fun EventHandlerScope<
            VerseDetailContract.Inputs,
            VerseDetailContract.Events,
            VerseDetailContract.State,
            >.handleEvent(
        event: VerseDetailContract.Events,
    ): Unit = when (event) {
        is VerseDetailContract.Events.NavigateTo -> {
            if (event.replaceTop) {
                router.send(RouterContract.Inputs.ReplaceTopDestination(event.destination))
            } else {
                router.send(RouterContract.Inputs.GoToDestination(event.destination))
            }
        }

        is VerseDetailContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
