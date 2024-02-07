package com.caseyjbrooks.verses.screens.detail

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.verses.models.VerseId
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal class VerseDetailKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::VerseDetailInputHandler)
        factoryOf(::VerseDetailEventHandler)

        factory<VerseDetailViewModel> { (viewModelCoroutineScope: CoroutineScope, verseId: VerseId) ->
            VerseDetailViewModel(
                coroutineScope = viewModelCoroutineScope,
                config = buildWithViewModel(
                    initialState = VerseDetailContract.State(verseId = verseId),
                    inputHandler = get<VerseDetailInputHandler>(),
                    name = "Verse Detail",
                ) {
                    VerseDetailContract.Inputs.ObserveVerse(verseId)
                },
                eventHandler = get<VerseDetailEventHandler>(),
            )
        }
    }
}
