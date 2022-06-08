package com.copperleaf.scripturenow.ui.verses.list

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.observeFlows
import com.copperleaf.scripturenow.repositories.verses.MemoryVersesRepository
import com.copperleaf.scripturenow.ui.Destinations
import kotlinx.coroutines.flow.map

class MemoryVerseListInputHandler(
    private val memoryVersesRepository: MemoryVersesRepository
) : InputHandler<
    MemoryVerseListContract.Inputs,
    MemoryVerseListContract.Events,
    MemoryVerseListContract.State> {
    override suspend fun InputHandlerScope<
        MemoryVerseListContract.Inputs,
        MemoryVerseListContract.Events,
        MemoryVerseListContract.State>.handleInput(
        input: MemoryVerseListContract.Inputs
    ) = when (input) {
        is MemoryVerseListContract.Inputs.Initialize -> {
            observeFlows(
                "focused memory verse",
                memoryVersesRepository
                    .getAllVerses(input.forceRefresh)
                    .map { MemoryVerseListContract.Inputs.VersesUpdated(it) }
            )
        }
        is MemoryVerseListContract.Inputs.VersesUpdated -> {
            updateState { it.copy(verses = input.verses) }
        }
        is MemoryVerseListContract.Inputs.CreateVerse -> {
            postEvent(
                MemoryVerseListContract.Events.NavigateTo(
                    Destinations.App.Verses.Create.directions()
                )
            )
        }
        is MemoryVerseListContract.Inputs.ViewVerse -> {
            postEvent(
                MemoryVerseListContract.Events.NavigateTo(
                    Destinations.App.Verses.Detail.directions(
                        pathParameters = mapOf(
                            "verseId" to listOf(input.verse.uuid.toString())
                        )
                    )
                )
            )
        }
        is MemoryVerseListContract.Inputs.EditVerse -> {
            postEvent(
                MemoryVerseListContract.Events.NavigateTo(
                    Destinations.App.Verses.Edit.directions(
                        pathParameters = mapOf(
                            "verseId" to listOf(input.verse.uuid.toString())
                        )
                    )
                )
            )
        }
        is MemoryVerseListContract.Inputs.DeleteVerse -> {
            // delete the verse
            memoryVersesRepository.deleteVerse(input.verse)

            // then exit the screen
            postEvent(MemoryVerseListContract.Events.NavigateBack)
        }
    }
}
