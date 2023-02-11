package com.caseyjbrooks.scripturenow.viewmodel.memory.list

import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

public class MemoryVerseListInputHandler(
    private val memoryVerseRepository: MemoryVerseRepository
) : InputHandler<
        MemoryVerseListContract.Inputs,
        MemoryVerseListContract.Events,
        MemoryVerseListContract.State> {
    override suspend fun InputHandlerScope<
            MemoryVerseListContract.Inputs,
            MemoryVerseListContract.Events,
            MemoryVerseListContract.State>.handleInput(
        input: MemoryVerseListContract.Inputs
    ): Unit = when (input) {
        is MemoryVerseListContract.Inputs.Initialize -> {
            observeFlows(
                "focused memory verse",
                memoryVerseRepository
                    .getAllVerses(input.forceRefresh)
                    .map { MemoryVerseListContract.Inputs.VersesUpdated(it) }
            )
        }

        is MemoryVerseListContract.Inputs.VersesUpdated -> {
            updateState { it.copy(verses = input.verses) }
        }

        is MemoryVerseListContract.Inputs.CreateVerse -> {
//            postEvent(
//                MemoryVerseListContract.Events.NavigateTo(
//                    Destinations.App.Verses.Create.directions()
//                )
//            )
        }

        is MemoryVerseListContract.Inputs.ViewVerse -> {
//            postEvent(
//                MemoryVerseListContract.Events.NavigateTo(
//                    Destinations.App.Verses.Detail.directions(
//                        pathParameters = mapOf(
//                            "verseId" to listOf(input.verse.uuid.toString())
//                        )
//                    )
//                )
//            )
        }

        is MemoryVerseListContract.Inputs.EditVerse -> {
//            postEvent(
//                MemoryVerseListContract.Events.NavigateTo(
//                    Destinations.App.Verses.Edit.directions(
//                        pathParameters = mapOf(
//                            "verseId" to listOf(input.verse.uuid.toString())
//                        )
//                    )
//                )
//            )
        }

        is MemoryVerseListContract.Inputs.DeleteVerse -> {
            // delete the verse
            memoryVerseRepository.deleteVerse(input.verse)

            // then exit the screen
            postEvent(MemoryVerseListContract.Events.NavigateBack)
        }
    }
}
