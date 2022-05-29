package com.copperleaf.beer.main

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

class MainInputHandler : InputHandler<
    MainContract.Inputs,
    MainContract.Events,
    MainContract.State> {
    override suspend fun InputHandlerScope<
        MainContract.Inputs,
        MainContract.Events,
        MainContract.State>.handleInput(
        input: MainContract.Inputs
    ) = when (input) {
        is MainContract.Inputs.UpdateState -> {
            println("updating VM state")
            updateState { input.fn(it) }
        }
    }
}
