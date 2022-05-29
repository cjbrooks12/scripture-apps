package com.copperleaf.scripturenow.ui.votd

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.plusAssign
import com.copperleaf.scripturenow.utils.BootstrapInterceptor
import kotlinx.coroutines.CoroutineScope

class VotdViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: VotdInputHandler,
    eventHandler: VotdEventHandler,
) : BasicViewModel<
    VotdContract.Inputs,
    VotdContract.Events,
    VotdContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor {
                VotdContract.Inputs.Initialize(false)
            }
        }
        .forViewModel(
            inputHandler = inputHandler,
            initialState = VotdContract.State(),
            name = "Votd",
        ),
    eventHandler = eventHandler,
)
