package com.caseyjbrooks.prayer.schedules

import com.caseyjbrooks.di.GlobalKoinApplication
import com.copperleaf.ballast.scheduler.SchedulerAdapter
import com.copperleaf.ballast.scheduler.SchedulerAdapterScope
import com.copperleaf.ballast.scheduler.schedule.EveryDaySchedule
import com.copperleaf.ballast.scheduler.schedule.FixedDelaySchedule
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.hours

internal class PrayerSchedulerAdapter : SchedulerAdapter<
        PrayerSchedulesContract.Inputs,
        PrayerSchedulesContract.Events,
        PrayerSchedulesContract.State> {

    override fun SchedulerAdapterScope<
            PrayerSchedulesContract.Inputs,
            PrayerSchedulesContract.Events,
            PrayerSchedulesContract.State>.configureSchedules() {
        val timeZone: TimeZone = GlobalKoinApplication.koinApplication.koin.get()

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
        onSchedule(
            key = "Notify to pray",
            schedule = EveryDaySchedule(LocalTime(12, 0), timeZone = timeZone),
            scheduledInput = { PrayerSchedulesContract.Inputs.PrayerNotification }
        )
    }
}
