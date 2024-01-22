package com.caseyjbrooks.prayer.domain.query

import com.caseyjbrooks.prayer.models.ArchiveStatus
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

/**
 * Fetch the entire list of saved prayers for display in the UI. The user may provide some filters to only view a subset
 * of the saved verses in their collection.
 *
 * Query the DB to view the full set or a subset of their saved verse collection. It may be fetched from a local
 * cache and be available offline, or from a remote source which would fail if there is no internet connection.
 *
 * [archiveStatus] must be set to include only those prayers that are not archived, those that are, or both.
 *
 * [tags] can be provided to futher filter the collection. If multiple tags are provided, only prayers matching ALL
 * gives tags will be returned. If tags is empty, no tag filtering will be performed.
 */
public interface QueryPrayersUseCase {
    public operator fun invoke(
        archiveStatus: ArchiveStatus,
        prayerType: Set<SavedPrayerType>,
        tags: Set<PrayerTag>,
    ): Flow<Cached<List<SavedPrayer>>>
}
