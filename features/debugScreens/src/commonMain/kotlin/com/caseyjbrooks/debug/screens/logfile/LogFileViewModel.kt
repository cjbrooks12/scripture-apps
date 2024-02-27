package com.caseyjbrooks.debug.screens.logfile

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class LogFileViewModel(
    config: BallastViewModelConfiguration<
            LogFileContract.Inputs,
            LogFileContract.Events,
            LogFileContract.State>,
    eventHandler: EventHandler<
            LogFileContract.Inputs,
            LogFileContract.Events,
            LogFileContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        LogFileContract.Inputs,
        LogFileContract.Events,
        LogFileContract.State>(
    config, eventHandler, coroutineScope
)
