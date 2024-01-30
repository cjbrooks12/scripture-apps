package com.caseyjbrooks.votd.repository

import com.caseyjbrooks.votd.models.VerseOfTheDay
import kotlinx.coroutines.flow.Flow

public interface VerseOfTheDayRepository {
    public suspend fun fetchAndCacheVerseOfTheDay(): Result<Unit>
    public fun getTodaysVerseOfTheDay(): Flow<VerseOfTheDay?>
}
