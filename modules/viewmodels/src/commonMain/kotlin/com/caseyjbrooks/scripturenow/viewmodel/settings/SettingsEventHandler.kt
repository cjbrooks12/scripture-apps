package com.caseyjbrooks.scripturenow.viewmodel.settings

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.vm.Router

@Suppress("UNUSED_EXPRESSION")
public class SettingsEventHandler(
    private val router: Router<ScriptureNowRoute>,
) : EventHandler<
        SettingsContract.Inputs,
        SettingsContract.Events,
        SettingsContract.State> {
    override suspend fun EventHandlerScope<
            SettingsContract.Inputs,
            SettingsContract.Events,
            SettingsContract.State>.handleEvent(
        event: SettingsContract.Events
    ): Unit = when (event) {
        else -> {}
    }
}
