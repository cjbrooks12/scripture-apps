package com.caseyjbrooks.prayer.screens.form

import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

internal class PrayerFormEventHandler(
    private val router: Router<ScriptureNowScreen>,
) : EventHandler<
        PrayerFormContract.Inputs,
        PrayerFormContract.Events,
        PrayerFormContract.State,> {
    override suspend fun EventHandlerScope<
            PrayerFormContract.Inputs,
            PrayerFormContract.Events,
            PrayerFormContract.State,>.handleEvent(
        event: PrayerFormContract.Events,
    ): Unit = when (event) {
        is PrayerFormContract.Events.NavigateTo -> {
            if (event.replaceTop) {
                router.send(RouterContract.Inputs.ReplaceTopDestination(event.destination))
            } else {
                router.send(RouterContract.Inputs.GoToDestination(event.destination))
            }
        }

        is PrayerFormContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
