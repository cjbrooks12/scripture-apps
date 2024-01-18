package com.caseyjbrooks.prayer.models

/**
 * Details about a Daily Prayer returned from some source of prayers.
 */
public data class DailyPrayer(
    /**
     * The text of the prayer. If a user chooses to save this prayer to the collection, this value will be copied to
     * [SavedPrayer.text].
     */
    val text: String,

    /**
     * The attribution text for this prayer, showing details of what organization or API supplied the data for this
     * prayer.
     */
    val attribution: String,

    /**
     * A list of tags which may be provided by the API for categorizing prayers.
     */
    val tags: List<PrayerTag>,
)
