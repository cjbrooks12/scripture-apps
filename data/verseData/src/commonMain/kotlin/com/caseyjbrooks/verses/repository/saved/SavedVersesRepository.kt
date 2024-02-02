package com.caseyjbrooks.verses.repository.saved

import com.caseyjbrooks.verses.models.ArchiveStatus
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.models.VerseTag
import kotlinx.coroutines.flow.Flow

/**
 * A wrapper around the primary database which stores the user's collection of saved verses.
 */
public interface SavedVersesRepository {

    /**
     * Add a new record to the DB with the contents of [verse]. This operation should fail if a verse with the same
     * [SavedVerse.uuid] already exists.
     */
    public suspend fun createVerse(verse: SavedVerse)

    /**
     * Update an existing record in the DB with the contents of [verse]. This operation should fail if a verse with
     * the provided [SavedVerse.uuid] does not exist. Use [SavedVersesRepository.createVerse] to create a new
     * record for verses with a new ID.
     */
    public suspend fun updateVerse(verse: SavedVerse)

    /**
     * Permanently delete a verse from the collection. This operation should fail if a verse with the provided
     * [SavedVerse.uuid] does not exist.
     *
     * This is not a soft-delete, it cannot be undone. For soft-deleting functionality, set the verse as archived
     * instead.
     */
    public suspend fun deleteVerse(verse: SavedVerse)

    /**
     * Return the list of all verses in the collection. This is a live query, and it should re-emit the entire
     * collection if the data in the underlying datasource changes.
     */
    public fun getVerses(
        archiveStatus: ArchiveStatus,
        tags: Set<VerseTag>,
    ): Flow<List<SavedVerse>>

    /**
     * Return a single verse by its [uuid], or null if no verse with that ID can be found in the collection. This is a
     * live query, and it should re-emit the entire collection if the data in the underlying datasource changes.
     */
    public fun getVerseById(uuid: VerseId): Flow<SavedVerse?>
}
