package com.caseyjbrooks.prayer.schedules

import com.copperleaf.ballast.scheduler.SchedulerAdapter
import com.copperleaf.ballast.scheduler.SchedulerAdapterScope
import com.copperleaf.ballast.scheduler.schedule.EveryDaySchedule
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

internal class PrayerSchedulerAdapter : SchedulerAdapter<
        PrayerSchedulesContract.Inputs,
        PrayerSchedulesContract.Events,
        PrayerSchedulesContract.State> {

    override fun SchedulerAdapterScope<
            PrayerSchedulesContract.Inputs,
            PrayerSchedulesContract.Events,
            PrayerSchedulesContract.State>.configureSchedules() {
        val timeZone = TimeZone.currentSystemDefault()

        onSchedule(
            key = "Auto-Archive Scheduled Prayers",
            schedule = EveryDaySchedule(LocalTime(0, 0), timeZone = timeZone),
            scheduledInput = { PrayerSchedulesContract.Inputs.ArchiveScheduledPrayers }
        )
        onSchedule(
            key = "Notify to pray",
            schedule = EveryDaySchedule(LocalTime(12, 0), timeZone = timeZone),
            scheduledInput = { PrayerSchedulesContract.Inputs.PrayerNotification }
        )
    }
}
