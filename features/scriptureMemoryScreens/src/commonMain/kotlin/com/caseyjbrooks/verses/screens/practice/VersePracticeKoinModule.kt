package com.caseyjbrooks.verses.screens.practice

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.verses.models.VerseId
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal class VersePracticeKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::VersePracticeInputHandler)
        factoryOf(::VersePracticeEventHandler)

        factory<VersePracticeViewModel> { (viewModelCoroutineScope: CoroutineScope, verseId: VerseId) ->
            VersePracticeViewModel(
                coroutineScope = viewModelCoroutineScope,
                config = buildWithViewModel(
                    initialState = VersePracticeContract.State(verseId = verseId),
                    inputHandler = get<VersePracticeInputHandler>(),
                    name = "Verse Practice",
                ) {
                    VersePracticeContract.Inputs.ObserveVerse(verseId)
                },
                eventHandler = get<VersePracticeEventHandler>(),
            )
        }
    }
}
