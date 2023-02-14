package com.caseyjbrooks.scripturenow.viewmodel.prayer.list

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

public class PrayerListEventHandler(
    private val router: Router<ScriptureNowRoute>
) : EventHandler<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State> {
    override suspend fun EventHandlerScope<
            PrayerListContract.Inputs,
            PrayerListContract.Events,
            PrayerListContract.State>.handleEvent(
        event: PrayerListContract.Events
    ): Unit = when (event) {
        is PrayerListContract.Events.NavigateTo -> {
            router.send(RouterContract.Inputs.GoToDestination(event.destination))
        }

        PrayerListContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
