package com.copperleaf.scripturenow.ui.home

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import kotlinx.coroutines.delay

class HomeInputHandler : InputHandler<
    HomeContract.Inputs,
    HomeContract.Events,
    HomeContract.State> {
    override suspend fun InputHandlerScope<
        HomeContract.Inputs,
        HomeContract.Events,
        HomeContract.State>.handleInput(
        input: HomeContract.Inputs
    ) = when (input) {
        is HomeContract.Inputs.Initialize -> {
            updateState { it.copy(loading = true) }
            delay(1000)
            updateState { it.copy(loading = false) }
        }
        is HomeContract.Inputs.GoBack -> {
            postEvent(HomeContract.Events.NavigateUp)
        }
    }
}
