package com.caseyjbrooks.votd.schedules

import com.caseyjbrooks.notifications.NotificationService
import com.caseyjbrooks.votd.domain.gettoday.GetTodaysVerseOfTheDayUseCase
import com.caseyjbrooks.votd.domain.prefetch.PrefetchVerseOfTheDayUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.repository.cache.awaitValue
import com.copperleaf.ballast.repository.cache.getValueOrNull

internal class VotdSchedulesInputHandler(
    private val notificationService: NotificationService,
    private val prefetchVerseOfTheDayUseCase: PrefetchVerseOfTheDayUseCase,
    private val getTodaysVerseOfTheDayUseCase: GetTodaysVerseOfTheDayUseCase,
) : InputHandler<
        VotdSchedulesContract.Inputs,
        VotdSchedulesContract.Events,
        VotdSchedulesContract.State> {
    override suspend fun InputHandlerScope<
            VotdSchedulesContract.Inputs,
            VotdSchedulesContract.Events,
            VotdSchedulesContract.State>.handleInput(
        input: VotdSchedulesContract.Inputs
    ): Unit = when (input) {
        VotdSchedulesContract.Inputs.FetchVotd -> {
            prefetchVerseOfTheDayUseCase()
        }

        VotdSchedulesContract.Inputs.VotdNotification -> {
            val result = getTodaysVerseOfTheDayUseCase().awaitValue().getValueOrNull()

            if (result != null) {
                notificationService.showNotification(
                    title = "Verse of the Day",
                    message = "${result.verse}\n-${result.reference}"
                )
            } else {
                notificationService.showNotification(
                    title = "Verse of the Day",
                    message = "Click to view today's verse."
                )
            }
        }
    }
}
