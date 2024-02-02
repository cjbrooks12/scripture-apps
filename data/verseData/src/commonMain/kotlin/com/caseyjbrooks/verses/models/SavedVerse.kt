package com.caseyjbrooks.verses.models

import kotlinx.datetime.Instant

/**
 * A verse that the user has saved to their collection, and wants to be reminded to pray for.
 */
public data class SavedVerse(

    /**
     * Unique ID for this verse. Used in URLs to lookup this verse in the database
     */
    val uuid: VerseId,

    /**
     * The Bible reference of the passage.
     */
    val reference: String,

    /**
     * The main text of the verse.
     */
    val text: String,

    /**
     * The user-specified type of verse, if it is categorized with a status.
     */
    val status: VerseStatus?,

    /**
     * A list of tags used to categorize verses
     */
    val tags: List<VerseTag>,

    /**
     * True if this verse has been archived
     */
    val archived: Boolean,

    /**
     * If this verse is archived, this will be the Instant at which the verse was marked as archived. If [archived] is
     * true, this value will be non-null. If it is not archived, this value will be null.
     */
    val archivedAt: Instant?,

    /**
     * The Instant this verse was first created. It will never be changed after creation.
     */
    val createdAt: Instant,

    /**
     * The last time this verse was updated, for any reason. This value will be updated every time a verse is saved.
     */
    val updatedAt: Instant,
)
