package com.caseyjbrooks.verses.domain.getbyid

import com.caseyjbrooks.verses.models.VerseId
import com.caseyjbrooks.verses.models.SavedVerse
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

/**
 * Fetch a single verse by its primary UUID, to display or interact with somewhere in the UI.
 *
 * Load a verse from the user's main collection by its UUID. It may be fetched from a local cache and be available
 * offline, or from a remote source which would fail if there is no internet connection.
 */
public interface GetVerseByIdUseCase {
    public operator fun invoke(verseId: VerseId): Flow<Cached<SavedVerse>>
}
