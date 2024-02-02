package com.caseyjbrooks.verses.screens.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class VerseListViewModel(
    config: BallastViewModelConfiguration<
            VerseListContract.Inputs,
            VerseListContract.Events,
            VerseListContract.State>,
    eventHandler: EventHandler<
            VerseListContract.Inputs,
            VerseListContract.Events,
            VerseListContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        VerseListContract.Inputs,
        VerseListContract.Events,
        VerseListContract.State>(
    config, eventHandler, coroutineScope
)
