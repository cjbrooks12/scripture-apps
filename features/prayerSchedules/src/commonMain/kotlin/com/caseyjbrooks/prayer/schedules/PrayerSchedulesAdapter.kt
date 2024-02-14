package com.caseyjbrooks.prayer.schedules

import co.touchlab.kermit.Logger
import com.caseyjbrooks.di.GlobalKoinApplication
import com.caseyjbrooks.prayer.domain.getwithnotifications.GetPrayersWithNotificationsUseCase
import com.caseyjbrooks.prayer.models.PrayerNotification
import com.copperleaf.ballast.scheduler.SchedulerAdapter
import com.copperleaf.ballast.scheduler.SchedulerAdapterScope
import com.copperleaf.ballast.scheduler.schedule.EveryDaySchedule
import com.copperleaf.ballast.scheduler.schedule.FixedDelaySchedule
import com.copperleaf.ballast.scheduler.schedule.FixedInstantSchedule
import com.copperleaf.ballast.scheduler.schedule.filterByDayOfWeek
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import org.koin.core.parameter.parametersOf
import kotlin.time.Duration.Companion.hours

internal class PrayerSchedulesAdapter : SchedulerAdapter<
        PrayerSchedulesContract.Inputs,
        PrayerSchedulesContract.Events,
        PrayerSchedulesContract.State> {

    override suspend fun SchedulerAdapterScope<
            PrayerSchedulesContract.Inputs,
            PrayerSchedulesContract.Events,
            PrayerSchedulesContract.State>.configureSchedules() {
        val koin = GlobalKoinApplication.koinApplication.koin
        val timeZone: TimeZone = koin.get()
        val getPrayersWithNotificationsUseCase: GetPrayersWithNotificationsUseCase = koin.get()
        val logger: Logger = koin.get { parametersOf("BallastWorkManager") }

        onSchedule(
            key = "Prefetch daily prayer",
            schedule = EveryDaySchedule(LocalTime(2, 30), timeZone = timeZone),
            scheduledInput = { PrayerSchedulesContract.Inputs.FetchDailyPrayer }
        )
        onSchedule(
            key = "Auto-Archive Scheduled Prayers",
            schedule = FixedDelaySchedule(4.hours),
            scheduledInput = { PrayerSchedulesContract.Inputs.ArchiveScheduledPrayers }
        )

        val prayersWithNotifications = getPrayersWithNotificationsUseCase()

        if (prayersWithNotifications.isEmpty()) {
            // There are no prayers with notifications, but still show a generic notification prompting to pray
            logger.i("Scheduling generic notification")
            onSchedule(
                key = "Notify to pray",
                schedule = EveryDaySchedule(LocalTime(12, 0), timeZone = timeZone),
                scheduledInput = { PrayerSchedulesContract.Inputs.GenericPrayerNotification }
            )
        } else {
            // set up a schedule for each prayer that has notifications
            logger.i("Scheduling ${prayersWithNotifications.size} prayer notifications")
            prayersWithNotifications.forEach { savedPrayer ->
                onSchedule(
                    key = "Prayer Notification (id=${savedPrayer.uuid.uuid})",
                    schedule = when (val notif = savedPrayer.notification) {
                        is PrayerNotification.None -> error("Should not get here")
                        is PrayerNotification.Once -> FixedInstantSchedule(notif.instant)
                        is PrayerNotification.Daily -> EveryDaySchedule(notif.time, timeZone = timeZone)
                            .filterByDayOfWeek(*notif.daysOfWeek.toTypedArray(), timeZone = timeZone)
                    },
                    scheduledInput = { PrayerSchedulesContract.Inputs.ScheduledPrayerNotification(savedPrayer.uuid) }
                )
            }
        }
    }
}
