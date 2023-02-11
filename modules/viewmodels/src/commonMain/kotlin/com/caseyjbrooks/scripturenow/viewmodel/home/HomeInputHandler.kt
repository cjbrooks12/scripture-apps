package com.caseyjbrooks.scripturenow.viewmodel.home

import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepository
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

public class HomeInputHandler(
    private val verseOfTheDayRepository: VerseOfTheDayRepository,
    private val memoryVerseRepository: MemoryVerseRepository,
) : InputHandler<
        HomeContract.Inputs,
        HomeContract.Events,
        HomeContract.State> {
    override suspend fun InputHandlerScope<
            HomeContract.Inputs,
            HomeContract.Events,
            HomeContract.State>.handleInput(
        input: HomeContract.Inputs
    ): Unit = when (input) {
        is HomeContract.Inputs.Initialize -> {
            observeFlows(
                "initialize",
                verseOfTheDayRepository
                    .getCurrentVerseOfTheDay(false)
                    .map { HomeContract.Inputs.VerseOfTheDayUpdated(it) },
//                memoryVerseRepository
//                    .getVerse(false)
//                    .map { HomeContract.Inputs.MemoryVerseUpdated(it) },
            )
        }

        is HomeContract.Inputs.VerseOfTheDayUpdated -> {
            updateState { it.copy(verseOfTheDay = input.verseOfTheDay) }
        }

        is HomeContract.Inputs.MemoryVerseUpdated -> {
            updateState { it.copy(memoryVerse = input.memoryVerse) }
        }
        is HomeContract.Inputs.VerseOfTheDayCardClicked -> {
            postEvent(HomeContract.Events.GoToVerseOfTheDay)
        }
    }
}
