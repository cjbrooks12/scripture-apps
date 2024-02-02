package com.caseyjbrooks.verses.screens.detail

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class VerseDetailViewModel(
    config: BallastViewModelConfiguration<
            VerseDetailContract.Inputs,
            VerseDetailContract.Events,
            VerseDetailContract.State>,
    eventHandler: EventHandler<
            VerseDetailContract.Inputs,
            VerseDetailContract.Events,
            VerseDetailContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        VerseDetailContract.Inputs,
        VerseDetailContract.Events,
        VerseDetailContract.State>(
    config, eventHandler, coroutineScope
)
