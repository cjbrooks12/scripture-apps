package com.caseyjbrooks.prayer.screens.list

import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

internal class PrayerListEventHandler(
    private val router: Router<ScriptureNowScreen>,
) : EventHandler<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State,
        > {
    override suspend fun EventHandlerScope<
            PrayerListContract.Inputs,
            PrayerListContract.Events,
            PrayerListContract.State,
            >.handleEvent(
        event: PrayerListContract.Events,
    ): Unit = when (event) {
        is PrayerListContract.Events.NavigateTo -> {
            if (event.replaceTop) {
                router.send(RouterContract.Inputs.ReplaceTopDestination(event.destination))
            } else {
                router.send(RouterContract.Inputs.GoToDestination(event.destination))
            }
        }

        is PrayerListContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
