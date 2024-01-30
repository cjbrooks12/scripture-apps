package com.caseyjbrooks.foryou.ui.dashboard

import com.caseyjbrooks.votd.domain.gettoday.GetTodaysVerseOfTheDayUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

internal class ForYouDashboardInputHandler(
    private val getTodaysVerseOfTheDayUseCase: GetTodaysVerseOfTheDayUseCase
) : InputHandler<
        ForYouDashboardContract.Inputs,
        ForYouDashboardContract.Events,
        ForYouDashboardContract.State> {
    override suspend fun InputHandlerScope<
            ForYouDashboardContract.Inputs,
            ForYouDashboardContract.Events,
            ForYouDashboardContract.State>.handleInput(
        input: ForYouDashboardContract.Inputs
    ): Unit = when (input) {
        is ForYouDashboardContract.Inputs.Initialize -> {
            observeFlows(
                "Initialize",
                getTodaysVerseOfTheDayUseCase()
                    .map { ForYouDashboardContract.Inputs.VerseOfTheDayUpdated(it) }
            )
        }

        is ForYouDashboardContract.Inputs.VerseOfTheDayUpdated -> {
            updateState { it.copy(verseOfTheDay = input.verseOfTheDay) }
        }

        is ForYouDashboardContract.Inputs.VerseOfTheDayCardClicked -> {
            noOp()
        }
    }
}
