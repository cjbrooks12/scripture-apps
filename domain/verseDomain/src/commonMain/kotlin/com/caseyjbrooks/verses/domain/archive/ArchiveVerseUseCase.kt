package com.caseyjbrooks.verses.domain.archive

import com.caseyjbrooks.verses.models.SavedVerse


/**
 * The user can move a verse to the "archive" so it's not normally displayed in their list of current verses. They
 * may also view their archives, and move a verse back to their main collection.
 *
 * Move the [verse] to the user's archives. This operation should succeed even if the user is offline, as the DB
 * will be synced back to the remote Source-Of-Truth once they're back online.
 */
public interface ArchiveVerseUseCase {
    public suspend operator fun invoke(verse: SavedVerse)
}
