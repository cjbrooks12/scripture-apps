package com.caseyjbrooks.verses.screens.detail

import com.caseyjbrooks.verses.domain.getbyid.GetVerseByIdUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import kotlinx.coroutines.flow.map

internal class VerseDetailInputHandler(
    private val getByIdUseCase: GetVerseByIdUseCase,
) : InputHandler<
        VerseDetailContract.Inputs,
        VerseDetailContract.Events,
        VerseDetailContract.State,
        > {
    override suspend fun InputHandlerScope<
            VerseDetailContract.Inputs,
            VerseDetailContract.Events,
            VerseDetailContract.State,
            >.handleInput(
        input: VerseDetailContract.Inputs,
    ): Unit = when (input) {
        is VerseDetailContract.Inputs.ObserveVerse -> {
            val currentState = updateStateAndGet { it.copy(verseId = input.verseId) }
            observeFlows(
                "ObserveVerse",
                getByIdUseCase(currentState.verseId)
                    .map { VerseDetailContract.Inputs.VerseUpdated(it) },
            )
        }

        is VerseDetailContract.Inputs.VerseUpdated -> {
            updateState { it.copy(cachedVerse = input.cachedVerses) }
        }

        is VerseDetailContract.Inputs.NavigateUp -> {
            postEvent(
                VerseDetailContract.Events.NavigateTo(
                    VerseDetailRoute.Directions.list(),
                    replaceTop = true,
                ),
            )
        }

        is VerseDetailContract.Inputs.GoBack -> {
            postEvent(
                VerseDetailContract.Events.NavigateBack,
            )
        }

        is VerseDetailContract.Inputs.Edit -> {
            val currentVerse = getCurrentState().cachedVerse.getCachedOrNull()

            if (currentVerse != null) {
                postEvent(
                    VerseDetailContract.Events.NavigateTo(
                        VerseDetailRoute.Directions.edit(currentVerse),
                        replaceTop = false,
                    ),
                )
            } else {
                noOp()
            }
        }

        is VerseDetailContract.Inputs.Practice -> {
            val currentVerse = getCurrentState().cachedVerse.getCachedOrNull()

            if (currentVerse != null) {
                postEvent(
                    VerseDetailContract.Events.NavigateTo(
                        VerseDetailRoute.Directions.practice(currentVerse),
                        replaceTop = false,
                    ),
                )
            } else {
                noOp()
            }
        }
    }
}
