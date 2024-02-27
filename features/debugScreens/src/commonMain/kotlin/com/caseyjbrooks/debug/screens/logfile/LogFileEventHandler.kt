package com.caseyjbrooks.debug.screens.logfile

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class LogFileEventHandler : EventHandler<
        LogFileContract.Inputs,
        LogFileContract.Events,
        LogFileContract.State> {
    override suspend fun EventHandlerScope<
            LogFileContract.Inputs,
            LogFileContract.Events,
            LogFileContract.State>.handleEvent(
        event: LogFileContract.Events
    ): Unit = when (event) {
        is LogFileContract.Events.NavigateUp -> {

        }
    }
}
