package com.caseyjbrooks.scripturenow.viewmodel.votd

import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

public typealias VerseOfTheDayViewModel = BasicViewModel<
        VerseOfTheDayContract.Inputs,
        VerseOfTheDayContract.Events,
        VerseOfTheDayContract.State>

public fun interface VerseOfTheDayViewModelProvider {
    public fun getVerseOfTheDayViewModel(coroutineScope: CoroutineScope): VerseOfTheDayViewModel
}
