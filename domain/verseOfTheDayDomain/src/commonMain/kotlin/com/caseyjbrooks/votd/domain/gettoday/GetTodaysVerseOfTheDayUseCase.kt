package com.caseyjbrooks.votd.domain.gettoday

import com.caseyjbrooks.votd.models.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

/**
 *
 */
public interface GetTodaysVerseOfTheDayUseCase {
    public suspend operator fun invoke(): Flow<Cached<VerseOfTheDay>>
}
