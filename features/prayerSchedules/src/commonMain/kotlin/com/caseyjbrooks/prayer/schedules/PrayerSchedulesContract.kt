package com.caseyjbrooks.prayer.schedules

internal object PrayerSchedulesContract {
    internal data class State(
        val loading: Boolean = false,
    )

    internal sealed interface Inputs {
        data object FetchDailyPrayer : Inputs
        data object ArchiveScheduledPrayers : Inputs
        data object PrayerNotification : Inputs
    }

    internal sealed interface Events {
    }
}
