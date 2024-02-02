package com.caseyjbrooks.verses.screens.form

import com.caseyjbrooks.routing.RouterViewModel
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract

internal class VerseFormEventHandler(
    private val router: RouterViewModel,
) : EventHandler<
        VerseFormContract.Inputs,
        VerseFormContract.Events,
        VerseFormContract.State,> {
    override suspend fun EventHandlerScope<
            VerseFormContract.Inputs,
            VerseFormContract.Events,
            VerseFormContract.State,>.handleEvent(
        event: VerseFormContract.Events,
    ): Unit = when (event) {
        is VerseFormContract.Events.NavigateTo -> {
            if (event.replaceTop) {
                router.send(RouterContract.Inputs.ReplaceTopDestination(event.destination))
            } else {
                router.send(RouterContract.Inputs.GoToDestination(event.destination))
            }
        }

        is VerseFormContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
