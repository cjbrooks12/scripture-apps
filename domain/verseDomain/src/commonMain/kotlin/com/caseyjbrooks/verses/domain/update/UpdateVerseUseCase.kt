package com.caseyjbrooks.verses.domain.update

import com.caseyjbrooks.verses.models.SavedVerse

/**
 * The user may make edits to their verses. This may be the result of the user directly editing the verse, or indirect
 * changes made as a result of other user-interactions or system processing.
 *
 * Create the [savedVerse] if it is not yet saved in the user's collection, otherwise update the existing record.
 *
 * The [SavedVerse.updatedAt] timestamp will be "touched" to denote the moment this change was saved.
 */
public interface UpdateVerseUseCase {
    public suspend operator fun invoke(verse: SavedVerse): SavedVerse
}
