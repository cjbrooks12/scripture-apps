package com.caseyjbrooks.verses.screens.practice

import com.caseyjbrooks.verses.domain.getbyid.GetVerseByIdUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

internal class VersePracticeInputHandler(
    private val getByIdUseCase: GetVerseByIdUseCase,
) : InputHandler<
        VersePracticeContract.Inputs,
        VersePracticeContract.Events,
        VersePracticeContract.State,
        > {
    override suspend fun InputHandlerScope<
            VersePracticeContract.Inputs,
            VersePracticeContract.Events,
            VersePracticeContract.State,
            >.handleInput(
        input: VersePracticeContract.Inputs,
    ): Unit = when (input) {
        is VersePracticeContract.Inputs.ObserveVerse -> {
            val currentState = updateStateAndGet { it.copy(verseId = input.verseId) }
            observeFlows(
                "ObserveVerse",
                getByIdUseCase(currentState.verseId)
                    .map { VersePracticeContract.Inputs.VerseUpdated(it) },
            )
        }

        is VersePracticeContract.Inputs.VerseUpdated -> {
            updateState { it.copy(cachedVerse = input.cachedVerses) }
        }

        is VersePracticeContract.Inputs.UpdateThreshold -> {
            updateState { it.copy(threshold = input.threshold) }
        }

        is VersePracticeContract.Inputs.TogglePeek -> {
            updateState { it.copy(peeking = !it.peeking) }
        }

        is VersePracticeContract.Inputs.NavigateUp -> {
            postEvent(
                VersePracticeContract.Events.NavigateTo(
                    VersePracticeRoute.Directions.list(),
                    replaceTop = true,
                ),
            )
        }

        is VersePracticeContract.Inputs.GoBack -> {
            postEvent(
                VersePracticeContract.Events.NavigateBack,
            )
        }
    }
}
