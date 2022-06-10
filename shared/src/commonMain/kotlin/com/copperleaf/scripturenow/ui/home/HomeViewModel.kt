package com.copperleaf.scripturenow.ui.home

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class HomeViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
) : BasicViewModel<
    HomeContract.Inputs,
    HomeContract.Events,
    HomeContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .forViewModel(
            inputHandler = HomeInputHandler(),
            initialState = HomeContract.State(),
            name = "Home",
        ),
    eventHandler = HomeEventHandler(),
)
