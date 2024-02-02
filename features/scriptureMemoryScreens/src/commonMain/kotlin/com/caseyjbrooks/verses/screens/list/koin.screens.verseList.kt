package com.caseyjbrooks.verses.screens.list

import com.caseyjbrooks.ballast.buildWithViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public val verseListScreenModule: Module = module {
    factoryOf(::VerseListInputHandler)
    factoryOf(::VerseListEventHandler)

    factory<VerseListViewModel> { (viewModelCoroutineScope: CoroutineScope) ->
        VerseListViewModel(
            coroutineScope = viewModelCoroutineScope,
            config = buildWithViewModel(
                initialState = VerseListContract.State(),
                inputHandler = get<VerseListInputHandler>(),
                name = "Verse List",
            ) {
                VerseListContract.Inputs.ObserveVerseList
            },
            eventHandler = get<VerseListEventHandler>(),
        )
    }
}
