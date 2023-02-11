package com.caseyjbrooks.scripturenow.viewmodel.memory.list

import com.copperleaf.ballast.core.BasicViewModel
import kotlinx.coroutines.CoroutineScope

public typealias MemoryVerseListViewModel = BasicViewModel<
        MemoryVerseListContract.Inputs,
        MemoryVerseListContract.Events,
        MemoryVerseListContract.State>

public fun interface MemoryVerseListViewModelProvider {
    public fun getMemoryVerseListViewModel(coroutineScope: CoroutineScope): MemoryVerseListViewModel
}
