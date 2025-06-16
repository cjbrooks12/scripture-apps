package com.caseyjbrooks.gradebook.queues.main

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import kotlinx.coroutines.delay

internal class ServerQueueInputHandler : InputHandler<
        ServerQueueContract.Inputs,
        ServerQueueContract.Events,
        ServerQueueContract.State> {
    override suspend fun InputHandlerScope<
            ServerQueueContract.Inputs,
            ServerQueueContract.Events,
            ServerQueueContract.State>.handleInput(
        input: ServerQueueContract.Inputs
    ): Unit = when (input) {
        is ServerQueueContract.Inputs.Increment -> {
            updateState { it.copy(count = it.count + 1) }
        }

        is ServerQueueContract.Inputs.Fail -> {
            error("Failed Job")
        }

        is ServerQueueContract.Inputs.Delayed -> {
            updateState { it.copy(count = it.count * 2) }
        }

        is ServerQueueContract.Inputs.Timeout -> {
            delay(input.duration)
            noOp()
        }
    }
}
