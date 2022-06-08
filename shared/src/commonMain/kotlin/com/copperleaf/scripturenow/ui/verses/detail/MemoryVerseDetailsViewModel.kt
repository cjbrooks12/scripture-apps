package com.copperleaf.scripturenow.ui.verses.detail

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class MemoryVerseDetailsViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: MemoryVerseDetailsInputHandler,
    eventHandler: MemoryVerseDetailsEventHandler,
) : BasicViewModel<
    MemoryVerseDetailsContract.Inputs,
    MemoryVerseDetailsContract.Events,
    MemoryVerseDetailsContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .forViewModel(
            inputHandler = inputHandler,
            initialState = MemoryVerseDetailsContract.State(),
            name = "MemoryVerseDetails",
        ),
    eventHandler = eventHandler,
)
