package com.caseyjbrooks.scripturenow.viewmodel.prayer.list

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

public class PrayerListEventHandler(
//    private val routerViewModel: MainRouterViewModel
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
//            routerViewModel.send(RouterContract.Inputs.GoToDestination(event.destination))
        }

        PrayerListContract.Events.NavigateBack -> {
//            routerViewModel.send(RouterContract.Inputs.GoBack)
        }
    }
}
