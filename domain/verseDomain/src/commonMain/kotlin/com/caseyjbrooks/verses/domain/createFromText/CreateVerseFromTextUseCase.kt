package com.caseyjbrooks.verses.domain.createFromText

import com.caseyjbrooks.verses.models.SavedVerse
import com.caseyjbrooks.verses.repository.config.VerseConfig

/**
 * Create a new verse from user-generated input and store it in the DB.
 *
 * If the user is on a free plan, the user is limited to at most [VerseConfig.maxVersesOnFreePlan] saved verses.
 * If they want more, they must subscribe, or delete some other verses. This method will throw an exception if the
 * user would exceed that limit.
 */
public interface CreateVerseFromTextUseCase {
    public suspend operator fun invoke(
        reference: String,
        text: String,
        tags: Set<String>
    ): SavedVerse
}
