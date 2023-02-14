package com.caseyjbrooks.scripturenow.viewmodel.votd

import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepository
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getValueOrThrow
import com.copperleaf.ballast.repository.cache.isLoading
import kotlinx.coroutines.flow.map

public class VerseOfTheDayInputHandler(
    private val verseOfTheDayRepository: VerseOfTheDayRepository,
    private val memoryVerseRepository: MemoryVerseRepository,
) : InputHandler<
        VerseOfTheDayContract.Inputs,
        VerseOfTheDayContract.Events,
        VerseOfTheDayContract.State> {
    override suspend fun InputHandlerScope<
            VerseOfTheDayContract.Inputs,
            VerseOfTheDayContract.Events,
            VerseOfTheDayContract.State>.handleInput(
        input: VerseOfTheDayContract.Inputs
    ): Unit = when (input) {
        is VerseOfTheDayContract.Inputs.Initialize -> {
            observeFlows(
                "Verse of the DaY",
                verseOfTheDayRepository
                    .getCurrentVerseOfTheDay(input.forceRefresh)
                    .map { VerseOfTheDayContract.Inputs.VerseOfTheDayUpdated(it) }
            )
        }

        is VerseOfTheDayContract.Inputs.VerseOfTheDayUpdated -> {
            updateState { it.copy(verseOfTheDay = input.verseOfTheDay) }

            input.verseOfTheDay.getCachedOrNull()?.let { votd ->
                observeFlows(
                    "Memory Verse for Verse of the Day",
                    memoryVerseRepository
                        .getVerseByReference(votd.reference, false)
                        .map { VerseOfTheDayContract.Inputs.SavedMemoryVerseUpdated(it) }
                )
            }

            Unit
        }

        is VerseOfTheDayContract.Inputs.SavedMemoryVerseUpdated -> {
            updateState { it.copy(savedMemoryVerse = input.memoryVerse) }
        }

        is VerseOfTheDayContract.Inputs.SaveAsMemoryVerse -> {
            val currentState = getCurrentState()

            if (currentState.verseOfTheDay.isLoading()) {
                // verse is not ready yet, ignore
            } else if (currentState.verseOfTheDay is Cached.FetchingFailed) {
                // verse could not be loaded, try again later
            } else {
                memoryVerseRepository.saveAsMemoryVerse(currentState.verseOfTheDay.getValueOrThrow())
            }
            noOp()
        }

        is VerseOfTheDayContract.Inputs.GoBack -> {
            postEvent(VerseOfTheDayContract.Events.NavigateUp)
        }
    }
}
