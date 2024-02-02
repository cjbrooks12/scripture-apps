package com.caseyjbrooks.verses.domain.query

import com.caseyjbrooks.verses.models.ArchiveStatus
import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.models.VerseTag
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

/**
 * Fetch the entire list of saved verses for display in the UI. The user may provide some filters to only view a subset
 * of the saved verses in their collection.
 *
 * Query the DB to view the full set or a subset of their saved verse collection. It may be fetched from a local
 * cache and be available offline, or from a remote source which would fail if there is no internet connection.
 *
 * [archiveStatus] must be set to include only those verses that are not archived, those that are, or both.
 *
 * [tags] can be provided to futher filter the collection. If multiple tags are provided, only verses matching ALL
 * gives tags will be returned. If tags is empty, no tag filtering will be performed.
 */
public interface QueryVersesUseCase {
    public operator fun invoke(
        archiveStatus: ArchiveStatus,
        tags: Set<VerseTag>,
    ): Flow<Cached<List<SavedVerse>>>
}
