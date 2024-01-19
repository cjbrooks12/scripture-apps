package com.caseyjbrooks.prayer.screens.detail

import com.caseyjbrooks.routing.RouterViewModel
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

internal class PrayerDetailEventHandler(
    private val router: RouterViewModel,
) : EventHandler<
        PrayerDetailContract.Inputs,
        PrayerDetailContract.Events,
        PrayerDetailContract.State,
        > {
    override suspend fun EventHandlerScope<
            PrayerDetailContract.Inputs,
            PrayerDetailContract.Events,
            PrayerDetailContract.State,
            >.handleEvent(
        event: PrayerDetailContract.Events,
    ): Unit = when (event) {
        is PrayerDetailContract.Events.NavigateTo -> {
            if (event.replaceTop) {
                router.send(RouterContract.Inputs.ReplaceTopDestination(event.destination))
            } else {
                router.send(RouterContract.Inputs.GoToDestination(event.destination))
            }
        }

        is PrayerDetailContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
