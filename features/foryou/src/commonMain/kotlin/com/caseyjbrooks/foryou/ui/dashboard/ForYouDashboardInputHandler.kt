package com.caseyjbrooks.foryou.ui.dashboard

import com.caseyjbrooks.prayer.domain.getdaily.GetDailyPrayerUseCase
import com.caseyjbrooks.votd.domain.gettoday.GetTodaysVerseOfTheDayUseCase
import com.caseyjbrooks.votd.domain.prefetch.PrefetchVerseOfTheDayUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

internal class ForYouDashboardInputHandler(
    private val prefetchVerseOfTheDayUseCase: PrefetchVerseOfTheDayUseCase,
    private val getTodaysVerseOfTheDayUseCase: GetTodaysVerseOfTheDayUseCase,
    private val getDailyPrayerUseCase: GetDailyPrayerUseCase,
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
            sideJob("prefetchVerseOfTheDayUseCase") {
                prefetchVerseOfTheDayUseCase()
            }

            observeFlows(
                "Initialize",
                getTodaysVerseOfTheDayUseCase()
                    .map { ForYouDashboardContract.Inputs.VerseOfTheDayUpdated(it) },
                getDailyPrayerUseCase()
                    .map { ForYouDashboardContract.Inputs.DailyPrayerUpdated(it) },
            )
        }

        is ForYouDashboardContract.Inputs.OverviewCardClicked -> {
            noOp()
        }

        is ForYouDashboardContract.Inputs.VerseOfTheDayUpdated -> {
            updateState { it.copy(verseOfTheDay = input.verseOfTheDay) }
        }

        is ForYouDashboardContract.Inputs.VerseOfTheDayCardClicked -> {
            noOp()
        }

        is ForYouDashboardContract.Inputs.DailyPrayerUpdated -> {
            updateState { it.copy(dailyPrayer = input.dailyPrayer) }
        }

        is ForYouDashboardContract.Inputs.DailyPrayerCardClicked -> {
            noOp()
        }

        is ForYouDashboardContract.Inputs.NoticeTextUpdated -> {
            updateState { it.copy(noticeText = input.noticeText) }
        }

        is ForYouDashboardContract.Inputs.NoticeCardClicked -> {
            noOp()
        }
    }
}
