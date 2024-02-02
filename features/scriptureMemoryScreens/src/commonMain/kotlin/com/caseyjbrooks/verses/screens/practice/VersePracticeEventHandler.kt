package com.caseyjbrooks.verses.screens.practice

import com.caseyjbrooks.routing.RouterViewModel
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract

internal class VersePracticeEventHandler(
    private val router: RouterViewModel,
) : EventHandler<
        VersePracticeContract.Inputs,
        VersePracticeContract.Events,
        VersePracticeContract.State,
        > {
    override suspend fun EventHandlerScope<
            VersePracticeContract.Inputs,
            VersePracticeContract.Events,
            VersePracticeContract.State,
            >.handleEvent(
        event: VersePracticeContract.Events,
    ): Unit = when (event) {
        is VersePracticeContract.Events.NavigateTo -> {
            if (event.replaceTop) {
                router.send(RouterContract.Inputs.ReplaceTopDestination(event.destination))
            } else {
                router.send(RouterContract.Inputs.GoToDestination(event.destination))
            }
        }

        is VersePracticeContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
