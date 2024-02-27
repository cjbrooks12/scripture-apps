package com.caseyjbrooks.debug.screens.logfile

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

internal class LogFileInputHandler : InputHandler<
        LogFileContract.Inputs,
        LogFileContract.Events,
        LogFileContract.State> {
    override suspend fun InputHandlerScope<
            LogFileContract.Inputs,
            LogFileContract.Events,
            LogFileContract.State>.handleInput(
        input: LogFileContract.Inputs
    ): Unit = when (input) {
        is LogFileContract.Inputs.Initialize -> {
            noOp()
        }

        is LogFileContract.Inputs.NavigateUp -> {
            noOp()
        }

        is LogFileContract.Inputs.GoBack -> {
            postEvent(LogFileContract.Events.NavigateUp)
        }
    }
}
