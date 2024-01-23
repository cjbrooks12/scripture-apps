package com.caseyjbrooks.prayer.models

import kotlinx.datetime.Instant

public sealed interface SavedPrayerType {
    /**
     * A regular prayer that stays in the user's collection until they choose to complete it manually.
     */
    public data object Persistent : SavedPrayerType

    /**
     * A prayer that will get automatically moved to the user's archives after the [completionDate] has passed.
     */
    public data class ScheduledCompletable(
        val completionDate: Instant,
    ) : SavedPrayerType
}
