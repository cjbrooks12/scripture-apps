package com.copperleaf.scripturenow.ui.verses.edit

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.forViewModel
import kotlinx.coroutines.CoroutineScope

class CreateOrEditMemoryVerseViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: CreateOrEditMemoryVerseInputHandler,
    eventHandler: CreateOrEditMemoryVerseEventHandler,
) : BasicViewModel<
    CreateOrEditMemoryVerseContract.Inputs,
    CreateOrEditMemoryVerseContract.Events,
    CreateOrEditMemoryVerseContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .forViewModel(
            inputHandler = inputHandler,
            initialState = CreateOrEditMemoryVerseContract.State(),
            name = "CreateOrEditMemoryVerse",
        ),
    eventHandler = eventHandler,
)
