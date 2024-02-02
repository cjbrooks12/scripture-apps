package com.caseyjbrooks.verses.domain.create

import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.repository.config.VerseConfig

/**
 * Create a new verse and insert it into the DB through.
 *
 * If the user is on a free plan, the user is limited to at most [VerseConfig.maxVersesOnFreePlan] saved verses.
 * If they want more, they must subscribe, or delete some other verses. This method will throw an exception if the
 * user would exceed that limit.
 */
public interface CreateVerseUseCase {
    public suspend operator fun invoke(verse: SavedVerse): SavedVerse
}
