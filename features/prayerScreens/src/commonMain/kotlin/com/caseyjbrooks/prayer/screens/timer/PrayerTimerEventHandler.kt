package com.caseyjbrooks.prayer.screens.timer

import com.caseyjbrooks.routing.RouterViewModel
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

internal class PrayerTimerEventHandler(
    private val router: RouterViewModel,
) : EventHandler<
        PrayerTimerContract.Inputs,
        PrayerTimerContract.Events,
        PrayerTimerContract.State,
        > {
    override suspend fun EventHandlerScope<
            PrayerTimerContract.Inputs,
            PrayerTimerContract.Events,
            PrayerTimerContract.State,
            >.handleEvent(
        event: PrayerTimerContract.Events,
    ): Unit = when (event) {
        is PrayerTimerContract.Events.NavigateTo -> {
            if (event.replaceTop) {
                router.send(RouterContract.Inputs.ReplaceTopDestination(event.destination))
            } else {
                router.send(RouterContract.Inputs.GoToDestination(event.destination))
            }
        }

        is PrayerTimerContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
