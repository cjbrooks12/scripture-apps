package com.caseyjbrooks.prayer.repository.saved

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import kotlinx.coroutines.flow.Flow

/**
 * A wrapper around the primary database which stores the user's collection of saved prayers.
 */
public interface SavedPrayersRepository {

    /**
     * Add a new record to the DB with the contents of [prayer]. This operation should fail if a prayer with the same
     * [SavedPrayer.uuid] already exists.
     */
    public suspend fun createPrayer(prayer: SavedPrayer)

    /**
     * Update an existing record in the DB with the contents of [prayer]. This operation should fail if a prayer with
     * the provided [SavedPrayer.uuid] does not exist. Use [SavedPrayersRepository.createPrayer] to create a new
     * record for prayers with a new ID.
     */
    public suspend fun updatePrayer(prayer: SavedPrayer)

    /**
     * Permanently delete a prayer from the collection. This operation should fail if a prayer with the provided
     * [SavedPrayer.uuid] does not exist.
     *
     * This is not a soft-delete, it cannot be undone. For soft-deleting functionality, set the prayer as archived
     * instead.
     */
    public suspend fun deletePrayer(prayer: SavedPrayer)

    /**
     * Return the list of all prayers in the collection. This is a live query, and it should re-emit the entire
     * collection if the data in the underlying datasource changes.
     */
    public fun getPrayers(
        archiveStatus: ArchiveStatus,
        prayerTypes: Set<SavedPrayerType>,
        tags: Set<PrayerTag>,
    ): Flow<List<SavedPrayer>>

    /**
     * Return a single prayer by its [uuid], or null if no prayer with that ID can be found in the collection. This is a
     * live query, and it should re-emit the entire collection if the data in the underlying datasource changes.
     */
    public fun getPrayerById(uuid: PrayerId): Flow<SavedPrayer?>

    /**
     * Return a single prayer by matching its [prayerText], or null if no prayer with the exact text can be found. This
     * is a live query, and it should re-emit the entire collection if the data in the underlying datasource changes.
     */
    public fun getPrayerByText(prayerText: String): Flow<SavedPrayer?>
}
