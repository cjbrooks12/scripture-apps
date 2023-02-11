package com.caseyjbrooks.scripturenow.viewmodel.prayer.detail

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

public class PrayerDetailsEventHandler(
//    private val routerViewModel: MainRouterViewModel
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
        is PrayerDetailsContract.Events.NavigateBack -> {
//            routerViewModel.send(RouterContract.Inputs.GoBack)
        }

        is PrayerDetailsContract.Events.NavigateTo -> {
//            routerViewModel.send(RouterContract.Inputs.GoToDestination(event.destination))
        }
    }
}
