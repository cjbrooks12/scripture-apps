package com.copperleaf.scripturenow.ui.verses.detail

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.scripturenow.repositories.verses.MemoryVersesRepository
import com.copperleaf.scripturenow.ui.Destinations
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
            postEvent(MemoryVerseDetailsContract.Events.NavigateBack)
        }
        is MemoryVerseDetailsContract.Inputs.EditVerse -> {
            val cachedVerse = getCurrentState().memoryVerse.getCachedOrNull()

            if (cachedVerse != null) {
                postEvent(
                    MemoryVerseDetailsContract.Events.NavigateTo(
                        Destinations.App.Verses.Edit.directions(
                            pathParameters = mapOf(
                                "verseId" to listOf(cachedVerse.uuid.toString())
                            )
                        )
                    )
                )
            } else {
                noOp()
            }
        }
        is MemoryVerseDetailsContract.Inputs.DeleteVerse -> {
            val cachedVerse = getCurrentState().memoryVerse.getCachedOrNull()

            if (cachedVerse != null) {
                memoryVersesRepository.deleteVerse(cachedVerse)
                postEvent(MemoryVerseDetailsContract.Events.NavigateBack)
            } else {
                noOp()
            }
        }
    }
}
