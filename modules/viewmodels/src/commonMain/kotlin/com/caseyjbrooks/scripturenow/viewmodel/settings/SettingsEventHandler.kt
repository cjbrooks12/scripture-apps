package com.caseyjbrooks.scripturenow.viewmodel.settings

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

@Suppress("UNUSED_EXPRESSION")
public class SettingsEventHandler : EventHandler<
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
