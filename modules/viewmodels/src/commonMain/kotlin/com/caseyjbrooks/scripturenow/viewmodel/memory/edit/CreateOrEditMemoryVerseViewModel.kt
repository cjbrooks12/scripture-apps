package com.caseyjbrooks.scripturenow.viewmodel.memory.edit

import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

public typealias CreateOrEditMemoryVerseViewModel = BasicViewModel<
        CreateOrEditMemoryVerseContract.Inputs,
        CreateOrEditMemoryVerseContract.Events,
        CreateOrEditMemoryVerseContract.State>

public fun interface CreateOrEditMemoryVerseViewModelProvider {
    public fun getCreateOrEditMemoryVerseViewModel(
        coroutineScope: CoroutineScope,
        verseId: String,
    ): CreateOrEditMemoryVerseViewModel
}
