package com.caseyjbrooks.prayer.models

import kotlinx.datetime.Instant

/**
 * A prayer that the user has saved to their collection, and wants to be reminded to pray for.
 */
public data class SavedPrayer(

    /**
     * Unique ID for this prayer. Used in URLs to lookup this prayer in the database
     */
    val uuid: PrayerId,

    /**
     * The main text of the prayer.
     */
    val text: String,

    /**
     * The user-specified type of prayer.
     */
    val prayerType: SavedPrayerType,

    /**
     * A list of tags used to categorize prayers
     */
    val tags: List<PrayerTag>,

    /**
     * True if this prayer has been archived
     */
    val archived: Boolean,

    /**
     * If this prayer is archived, this will be the Instant at which the prayer was marked as archived. If [archived] is
     * true, this value will be non-null. If it is not archived, this value will be null.
     */
    val archivedAt: Instant?,

    /**
     * The Instant this prayer was first created. It will never be changed after creation.
     */
    val createdAt: Instant,

    /**
     * The last time this prayer was updated, for any reason. This value will be updated every time a prayer is saved.
     */
    val updatedAt: Instant,
)
