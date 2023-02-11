package com.caseyjbrooks.scripturenow.viewmodel.prayer.edit

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

public class CreateOrEditPrayerEventHandler(
//    private val routerViewModel: MainRouterViewModel
) : EventHandler<
        CreateOrEditPrayerContract.Inputs,
        CreateOrEditPrayerContract.Events,
        CreateOrEditPrayerContract.State> {
    override suspend fun EventHandlerScope<
            CreateOrEditPrayerContract.Inputs,
            CreateOrEditPrayerContract.Events,
            CreateOrEditPrayerContract.State>.handleEvent(
        event: CreateOrEditPrayerContract.Events
    ): Unit = when (event) {
        is CreateOrEditPrayerContract.Events.NavigateUp -> {
//            routerViewModel.send(RouterContract.Inputs.GoBack)
        }
    }
}
