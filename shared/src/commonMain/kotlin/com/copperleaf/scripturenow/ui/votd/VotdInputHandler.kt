package com.copperleaf.scripturenow.ui.votd

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.scripturenow.repositories.votd.VerseOfTheDayRepository
import kotlinx.coroutines.flow.map

class VotdInputHandler(
    private val repository: VerseOfTheDayRepository
) : InputHandler<
    VotdContract.Inputs,
    VotdContract.Events,
    VotdContract.State> {
    override suspend fun InputHandlerScope<
        VotdContract.Inputs,
        VotdContract.Events,
        VotdContract.State>.handleInput(
        input: VotdContract.Inputs
    ) = when (input) {
        is VotdContract.Inputs.Initialize -> {
            observeFlows(
                "votd",
                repository
                    .getVerseOfTheDay(input.forceRefresh)
                    .map { VotdContract.Inputs.VerseOfTheDayChanged(it) }
            )
        }
        is VotdContract.Inputs.VerseOfTheDayChanged -> {
            updateState { it.copy(verseOfTheDay = input.verseOfTheDay) }
        }
        is VotdContract.Inputs.GoBack -> {
            postEvent(VotdContract.Events.NavigateUp)
        }
    }
}
