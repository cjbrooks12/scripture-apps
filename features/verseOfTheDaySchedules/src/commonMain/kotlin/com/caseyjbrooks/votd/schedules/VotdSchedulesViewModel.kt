package com.caseyjbrooks.votd.schedules

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class VotdSchedulesViewModel(
    config: BallastViewModelConfiguration<
            VotdSchedulesContract.Inputs,
            VotdSchedulesContract.Events,
            VotdSchedulesContract.State>,
    eventHandler: EventHandler<
            VotdSchedulesContract.Inputs,
            VotdSchedulesContract.Events,
            VotdSchedulesContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        VotdSchedulesContract.Inputs,
        VotdSchedulesContract.Events,
        VotdSchedulesContract.State>(
    config, eventHandler, coroutineScope
)
