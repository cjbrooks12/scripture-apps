package com.caseyjbrooks.prayer.schedules

import com.caseyjbrooks.notifications.NotificationService
import com.caseyjbrooks.prayer.domain.autoarchive.AutoArchivePrayersUseCase
import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.repository.cache.awaitValue
import com.copperleaf.ballast.repository.cache.getCachedOrThrow

internal class PrayerSchedulesInputHandler(
    private val notificationService: NotificationService,
    private val archivePrayersUseCase: AutoArchivePrayersUseCase,
    private val getPrayerByIdUseCase: GetPrayerByIdUseCase,
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
        is PrayerSchedulesContract.Inputs.ArchiveScheduledPrayers -> {
            noOp()
            archivePrayersUseCase.invoke()
        }

        is PrayerSchedulesContract.Inputs.GenericPrayerNotification -> {
            noOp()
            notificationService.showNotification(
                channelId = "Prayer",
                notificationId = "Prayer",
                title = "Abide",
                message = "Why don't you take a minute to pray right now?"
            )
        }

        is PrayerSchedulesContract.Inputs.ScheduledPrayerNotification -> {
            noOp()
            val prayer = getPrayerByIdUseCase(input.prayerId).awaitValue().getCachedOrThrow()
            notificationService.showNotification(
                channelId = "Prayer",
                notificationId = "Prayer Notification (id=${input.prayerId.uuid})",
                title = "Prayer Notification",
                message = prayer.text,
            )
        }

        is PrayerSchedulesContract.Inputs.FetchDailyPrayer -> {
            noOp()
        }
    }
}
