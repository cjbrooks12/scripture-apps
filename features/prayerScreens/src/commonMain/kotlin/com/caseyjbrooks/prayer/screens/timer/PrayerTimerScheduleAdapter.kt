package com.caseyjbrooks.prayer.screens.timer

import com.copperleaf.ballast.scheduler.SchedulerAdapter
import com.copperleaf.ballast.scheduler.SchedulerAdapterScope
import com.copperleaf.ballast.scheduler.schedule.EverySecondSchedule

internal class PrayerTimerScheduleAdapter : SchedulerAdapter<
        PrayerTimerContract.Inputs,
        PrayerTimerContract.Events,
        PrayerTimerContract.State,
        > {

    companion object {
        internal const val SCHEDULE_KEY = "timer tick"
    }

    override fun SchedulerAdapterScope<
            PrayerTimerContract.Inputs,
            PrayerTimerContract.Events,
            PrayerTimerContract.State>.configureSchedules() {
        onSchedule(
            key = SCHEDULE_KEY,
            schedule = EverySecondSchedule(),
        ) {
            PrayerTimerContract.Inputs.OnTimerTick
        }
    }
}
