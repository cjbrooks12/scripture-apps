package com.caseyjbrooks.debug.screens.loglist

import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope

internal class LogListEventHandler : EventHandler<
        LogListContract.Inputs,
        LogListContract.Events,
        LogListContract.State> {
    override suspend fun EventHandlerScope<
            LogListContract.Inputs,
            LogListContract.Events,
            LogListContract.State>.handleEvent(
        event: LogListContract.Events
    ): Unit = when (event) {
        is LogListContract.Events.NavigateUp -> {

        }
    }
}
