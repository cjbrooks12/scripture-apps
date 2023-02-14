package com.caseyjbrooks.scripturenow.viewmodel.prayer.detail

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

public class PrayerDetailsEventHandler(
    private val router: Router<ScriptureNowRoute>,
) : EventHandler<
        PrayerDetailsContract.Inputs,
        PrayerDetailsContract.Events,
        PrayerDetailsContract.State> {
    override suspend fun EventHandlerScope<
            PrayerDetailsContract.Inputs,
            PrayerDetailsContract.Events,
            PrayerDetailsContract.State>.handleEvent(
        event: PrayerDetailsContract.Events
    ): Unit = when (event) {
        is PrayerDetailsContract.Events.NavigateTo -> {
            router.send(RouterContract.Inputs.GoToDestination(event.destination))
        }

        is PrayerDetailsContract.Events.NavigateBack -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
