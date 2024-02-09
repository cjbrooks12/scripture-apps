package com.caseyjbrooks.prayer.schedules

import com.caseyjbrooks.prayer.models.PrayerId

internal object PrayerSchedulesContract {
    internal data class State(
        val loading: Boolean = false,
    )

    internal sealed interface Inputs {
        data object FetchDailyPrayer : Inputs
        data object ArchiveScheduledPrayers : Inputs
        data object GenericPrayerNotification : Inputs
        data class ScheduledPrayerNotification(val prayerId: PrayerId) : Inputs
    }

    internal sealed interface Events
}
