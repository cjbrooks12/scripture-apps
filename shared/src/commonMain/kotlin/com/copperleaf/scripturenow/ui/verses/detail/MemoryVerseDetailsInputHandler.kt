package com.copperleaf.scripturenow.ui.verses.detail

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.scripturenow.repositories.verses.MemoryVersesRepository
import kotlinx.coroutines.flow.map

class MemoryVerseDetailsInputHandler(
    private val memoryVersesRepository: MemoryVersesRepository
) : InputHandler<
    MemoryVerseDetailsContract.Inputs,
    MemoryVerseDetailsContract.Events,
    MemoryVerseDetailsContract.State> {
    override suspend fun InputHandlerScope<
        MemoryVerseDetailsContract.Inputs,
        MemoryVerseDetailsContract.Events,
        MemoryVerseDetailsContract.State>.handleInput(
        input: MemoryVerseDetailsContract.Inputs
    ) = when (input) {
        is MemoryVerseDetailsContract.Inputs.Initialize -> {
            observeFlows(
                "focused memory verse",
                memoryVersesRepository
                    .getVerse(input.verseUuid)
                    .map { MemoryVerseDetailsContract.Inputs.MemoryVerseUpdated(it) }
            )
        }
        is MemoryVerseDetailsContract.Inputs.MemoryVerseUpdated -> {
            updateState { it.copy(memoryVerse = input.memoryVerse) }
        }
        is MemoryVerseDetailsContract.Inputs.GoBack -> {

        }
    }
}
