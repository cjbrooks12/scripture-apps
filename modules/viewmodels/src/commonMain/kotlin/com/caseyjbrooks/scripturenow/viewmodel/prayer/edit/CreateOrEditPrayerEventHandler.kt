package com.caseyjbrooks.scripturenow.viewmodel.prayer.edit

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.Router

public class CreateOrEditPrayerEventHandler(
    private val router: Router<ScriptureNowRoute>
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
        is CreateOrEditPrayerContract.Events.NavigateTo -> {
            router.send(RouterContract.Inputs.GoToDestination(event.destination))
        }

        is CreateOrEditPrayerContract.Events.NavigateUp -> {
            router.send(RouterContract.Inputs.GoBack())
        }
    }
}
