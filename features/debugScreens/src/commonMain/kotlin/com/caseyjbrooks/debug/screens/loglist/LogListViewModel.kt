package com.caseyjbrooks.debug.screens.loglist

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class LogListViewModel(
    config: BallastViewModelConfiguration<
            LogListContract.Inputs,
            LogListContract.Events,
            LogListContract.State>,
    eventHandler: EventHandler<
            LogListContract.Inputs,
            LogListContract.Events,
            LogListContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        LogListContract.Inputs,
        LogListContract.Events,
        LogListContract.State>(
    config, eventHandler, coroutineScope
)
