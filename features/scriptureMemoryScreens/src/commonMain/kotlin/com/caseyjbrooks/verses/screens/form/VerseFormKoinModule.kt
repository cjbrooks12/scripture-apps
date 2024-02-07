package com.caseyjbrooks.verses.screens.form

import com.caseyjbrooks.ballast.buildWithViewModel
import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.verses.models.VerseId
import kotlinx.coroutines.CoroutineScope
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal class VerseFormKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::VerseFormInputHandler)
        factoryOf(::VerseFormEventHandler)

        factory<VerseFormViewModel> { (viewModelCoroutineScope: CoroutineScope, verseId: VerseId?) ->
            VerseFormViewModel(
                coroutineScope = viewModelCoroutineScope,
                config = buildWithViewModel(
                    initialState = VerseFormContract.State(verseId = verseId),
                    inputHandler = get<VerseFormInputHandler>(),
                    name = if (verseId != null) "Edit Verse $verseId" else "Create Verse",
                ) {
                    VerseFormContract.Inputs.ObserveVerse(verseId)
                },
                eventHandler = get<VerseFormEventHandler>(),
            )
        }
    }
}
