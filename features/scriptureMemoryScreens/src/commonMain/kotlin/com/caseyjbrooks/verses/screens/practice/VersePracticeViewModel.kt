package com.caseyjbrooks.verses.screens.practice

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class VersePracticeViewModel(
    config: BallastViewModelConfiguration<
            VersePracticeContract.Inputs,
            VersePracticeContract.Events,
            VersePracticeContract.State>,
    eventHandler: EventHandler<
            VersePracticeContract.Inputs,
            VersePracticeContract.Events,
            VersePracticeContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        VersePracticeContract.Inputs,
        VersePracticeContract.Events,
        VersePracticeContract.State>(
    config, eventHandler, coroutineScope
)
