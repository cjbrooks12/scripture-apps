package com.caseyjbrooks.foryou.ui.dashboard

import com.caseyjbrooks.domain.bus.EventBus
import com.caseyjbrooks.notifications.NotificationService
import com.caseyjbrooks.notifications.PermissionsDomainEvents
import com.caseyjbrooks.prayer.domain.getdaily.GetDailyPrayerUseCase
import com.caseyjbrooks.votd.domain.gettoday.GetTodaysVerseOfTheDayUseCase
import com.caseyjbrooks.votd.domain.prefetch.PrefetchVerseOfTheDayUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class ForYouDashboardInputHandler(
    private val prefetchVerseOfTheDayUseCase: PrefetchVerseOfTheDayUseCase,
    private val getTodaysVerseOfTheDayUseCase: GetTodaysVerseOfTheDayUseCase,
    private val getDailyPrayerUseCase: GetDailyPrayerUseCase,
    private val notificationService: NotificationService,
    private val eventBus: EventBus,
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
            updateState { it.copy(notificationsEnabled = notificationService.isPermissionGranted()) }

            sideJob("prefetchVerseOfTheDayUseCase") {
                prefetchVerseOfTheDayUseCase()
            }

            observeFlows(
                "Initialize",
                getTodaysVerseOfTheDayUseCase()
                    .map { ForYouDashboardContract.Inputs.VerseOfTheDayUpdated(it) },
                getDailyPrayerUseCase()
                    .map { ForYouDashboardContract.Inputs.DailyPrayerUpdated(it) },
                eventBus.events
                    .onEach { logger.debug("receiving event: $it") }
                    .filterIsInstance<PermissionsDomainEvents.PermissionResult>()
                    .map { ForYouDashboardContract.Inputs.NotificationPermissionUpdated(notificationService.isPermissionGranted()) },
            )
        }

        is ForYouDashboardContract.Inputs.NotificationPermissionUpdated -> {
            updateState { it.copy(notificationsEnabled = input.notificationsEnabled) }
        }

        is ForYouDashboardContract.Inputs.ShowNotificationPermissionPrompt -> {
            noOp()
            notificationService.promptForPermission()
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
