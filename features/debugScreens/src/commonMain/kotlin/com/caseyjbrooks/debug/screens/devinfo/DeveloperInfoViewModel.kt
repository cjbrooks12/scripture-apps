package com.caseyjbrooks.debug.screens.devinfo

import com.copperleaf.ballast.BallastViewModel
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class DeveloperInfoViewModel(
    config: BallastViewModelConfiguration<
            DeveloperInfoContract.Inputs,
            DeveloperInfoContract.Events,
            DeveloperInfoContract.State>,
    eventHandler: EventHandler<
            DeveloperInfoContract.Inputs,
            DeveloperInfoContract.Events,
            DeveloperInfoContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        DeveloperInfoContract.Inputs,
        DeveloperInfoContract.Events,
        DeveloperInfoContract.State>(
    config, eventHandler, coroutineScope
)
