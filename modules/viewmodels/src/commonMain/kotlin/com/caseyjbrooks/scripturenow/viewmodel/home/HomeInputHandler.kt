package com.caseyjbrooks.scripturenow.viewmodel.home

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepository
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.getCachedOrNull
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
                memoryVerseRepository
                    .getMainVerse(false)
                    .map { HomeContract.Inputs.MainMemoryVerseUpdated(it) },
            )
        }

        is HomeContract.Inputs.VerseOfTheDayUpdated -> {
            updateState { it.copy(verseOfTheDay = input.verseOfTheDay) }
        }

        is HomeContract.Inputs.MainMemoryVerseUpdated -> {
            updateState { it.copy(mainMemoryVerse = input.mainMemoryVerse) }
        }

        is HomeContract.Inputs.VerseOfTheDayCardClicked -> {
            postEvent(
                HomeContract.Events.NavigateTo(
                    ScriptureNowRoute.VerseOfTheDay
                        .directions()
                        .build()
                )
            )
        }

        is HomeContract.Inputs.MemoryVerseCardClicked -> {
            val currentMemoryVerse = getCurrentState().mainMemoryVerse.getCachedOrNull()

            if (currentMemoryVerse != null) {
                postEvent(
                    HomeContract.Events.NavigateTo(
                        ScriptureNowRoute.MemoryVerseDetails
                            .directions()
                            .path(currentMemoryVerse.uuid.toString())
                            .build()
                    )
                )
            } else {
                postEvent(
                    HomeContract.Events.NavigateTo(
                        ScriptureNowRoute.MemoryVerseList
                            .directions()
                            .build()
                    )
                )
            }
        }
    }
}
