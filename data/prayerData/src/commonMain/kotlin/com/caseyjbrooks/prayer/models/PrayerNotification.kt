package com.caseyjbrooks.prayer.models

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime

/**
 * A schedule to send reminder notifications for this specific prayer.
 */
public sealed interface PrayerNotification {
    /**
     * Don't send any notifications for this prayer
     */
    public data object None : PrayerNotification

    /**
     * Send a notification once, at a specific date and time.
     */
    public data class Once(val instant: Instant) : PrayerNotification

    /**
     * Send notifications every day at a specific time for specific days of the week.
     */
    public data class Daily(val daysOfWeek: Set<DayOfWeek>, val time: LocalTime) : PrayerNotification
}
