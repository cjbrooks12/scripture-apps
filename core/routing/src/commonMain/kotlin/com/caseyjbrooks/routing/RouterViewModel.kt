package com.caseyjbrooks.routing

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.vm.BasicRouter
import kotlinx.coroutines.CoroutineScope

public class RouterViewModel(
    config: BallastViewModelConfiguration<
            RouterContract.Inputs<ScriptureNowScreen>,
            RouterContract.Events<ScriptureNowScreen>,
            RouterContract.State<ScriptureNowScreen>>,
    coroutineScope: CoroutineScope,
) : BasicRouter<ScriptureNowScreen>(
    config = config,
    eventHandler = eventHandler { },
    coroutineScope = coroutineScope,
)
