package com.caseyjbrooks.prayer.schedules

import com.caseyjbrooks.notifications.NotificationService
import com.caseyjbrooks.prayer.domain.autoarchive.AutoArchivePrayersUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope

internal class PrayerSchedulesInputHandler(
    private val notificationService: NotificationService,
    private val archivePrayersUseCase: AutoArchivePrayersUseCase,
) : InputHandler<
        PrayerSchedulesContract.Inputs,
        PrayerSchedulesContract.Events,
        PrayerSchedulesContract.State> {
    override suspend fun InputHandlerScope<
            PrayerSchedulesContract.Inputs,
            PrayerSchedulesContract.Events,
            PrayerSchedulesContract.State>.handleInput(
        input: PrayerSchedulesContract.Inputs
    ): Unit = when (input) {
        PrayerSchedulesContract.Inputs.ArchiveScheduledPrayers -> {
            archivePrayersUseCase.invoke()
        }

        PrayerSchedulesContract.Inputs.PrayerNotification -> {
            notificationService.showNotification(
                title = "Scripture Now",
                message = "Why don't you take a minute to pray right now?"
            )
        }

        PrayerSchedulesContract.Inputs.FetchDailyPrayer -> {
            noOp()
        }
    }
}