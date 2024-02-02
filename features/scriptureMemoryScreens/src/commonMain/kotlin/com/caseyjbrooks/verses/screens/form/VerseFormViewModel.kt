package com.caseyjbrooks.verses.screens.form

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

internal class VerseFormViewModel(
    config: BallastViewModelConfiguration<
            VerseFormContract.Inputs,
            VerseFormContract.Events,
            VerseFormContract.State>,
    eventHandler: EventHandler<
            VerseFormContract.Inputs,
            VerseFormContract.Events,
            VerseFormContract.State>,
    coroutineScope: CoroutineScope,
) : BasicViewModel<
        VerseFormContract.Inputs,
        VerseFormContract.Events,
        VerseFormContract.State>(
    config, eventHandler, coroutineScope
)
