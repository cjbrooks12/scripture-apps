package com.caseyjbrooks.verses.screens.practice

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.screens.practice.VersePracticeContract
import com.caseyjbrooks.verses.screens.practice.VersePracticeEventHandler
import com.caseyjbrooks.verses.screens.practice.VersePracticeInputHandler
import com.caseyjbrooks.verses.screens.practice.VersePracticeViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

public val versePracticeScreenModule: Module = module {
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
