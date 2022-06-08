package com.copperleaf.scripturenow.ui.verses.list

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.plusAssign
import kotlinx.coroutines.CoroutineScope

class MemoryVerseListViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: MemoryVerseListInputHandler,
    eventHandler: MemoryVerseListEventHandler,
) : BasicViewModel<
    MemoryVerseListContract.Inputs,
    MemoryVerseListContract.Events,
    MemoryVerseListContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor {
                MemoryVerseListContract.Inputs.Initialize(false)
            }
        }
        .forViewModel(
            inputHandler = inputHandler,
            initialState = MemoryVerseListContract.State(),
            name = "MemoryVerseList",
        ),
    eventHandler = eventHandler,
)
