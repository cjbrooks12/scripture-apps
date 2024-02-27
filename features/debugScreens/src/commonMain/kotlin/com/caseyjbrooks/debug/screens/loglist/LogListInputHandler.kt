package com.caseyjbrooks.debug.screens.loglist

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

internal class LogListInputHandler : InputHandler<
        LogListContract.Inputs,
        LogListContract.Events,
        LogListContract.State> {
    override suspend fun InputHandlerScope<
            LogListContract.Inputs,
            LogListContract.Events,
            LogListContract.State>.handleInput(
        input: LogListContract.Inputs
    ): Unit = when (input) {
        is LogListContract.Inputs.Initialize -> {
            noOp()
        }

        is LogListContract.Inputs.NavigateUp -> {
            noOp()
        }

        is LogListContract.Inputs.GoBack -> {
            postEvent(LogListContract.Events.NavigateUp)
        }
    }
}
