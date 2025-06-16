package com.caseyjbrooks.gradebook.schedules.main

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

internal class ServerSchedulesInputHandler : InputHandler<
        ServerSchedulesContract.Inputs,
        ServerSchedulesContract.Events,
        ServerSchedulesContract.State> {
    override suspend fun InputHandlerScope<
            ServerSchedulesContract.Inputs,
            ServerSchedulesContract.Events,
            ServerSchedulesContract.State>.handleInput(
        input: ServerSchedulesContract.Inputs
    ): Unit = when (input) {
        is ServerSchedulesContract.Inputs.CleanCache -> {
            logger.info("Cleaning cache: ${input.key}")
            noOp()
        }
    }
}
