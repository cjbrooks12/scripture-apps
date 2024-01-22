package com.caseyjbrooks.votd.schedules

import com.copperleaf.ballast.scheduler.SchedulerAdapter
import com.copperleaf.ballast.scheduler.SchedulerAdapterScope
import com.copperleaf.ballast.scheduler.schedule.EveryDaySchedule
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

internal class VotdSchedulerAdapter : SchedulerAdapter<
        VotdSchedulesContract.Inputs,
        VotdSchedulesContract.Events,
        VotdSchedulesContract.State> {

    override fun SchedulerAdapterScope<
            VotdSchedulesContract.Inputs,
            VotdSchedulesContract.Events,
            VotdSchedulesContract.State>.configureSchedules() {
        val timeZone = TimeZone.currentSystemDefault()

        onSchedule(
            key = "Fetch Verse of the day",
            schedule = EveryDaySchedule(LocalTime(2, 30), timeZone = timeZone),
            scheduledInput = { VotdSchedulesContract.Inputs.FetchVotd }
        )
        onSchedule(
            key = "Show Verse of the Day notification",
            schedule = EveryDaySchedule(LocalTime(9, 0), timeZone = timeZone),
            scheduledInput = { VotdSchedulesContract.Inputs.VotdNotification }
        )
    }
}
