package com.caseyjbrooks.scripturenow.viewmodel.memory.detail

import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

public typealias MemoryVerseDetailsViewModel = BasicViewModel<
        MemoryVerseDetailsContract.Inputs,
        MemoryVerseDetailsContract.Events,
        MemoryVerseDetailsContract.State>

public fun interface MemoryVerseDetailsViewModelProvider {
    public fun getMemoryVerseDetailsViewModel(
        coroutineScope: CoroutineScope,
        verseId: String,
    ): MemoryVerseDetailsViewModel
}
