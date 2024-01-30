package com.caseyjbrooks.votd.repository

import com.caseyjbrooks.votd.models.VerseOfTheDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeVerseOfTheDayRepository(
    private val verseOfTheDay: VerseOfTheDay
) : VerseOfTheDayRepository {

    override suspend fun fetchAndCacheVerseOfTheDay(): Result<Unit> {
        return runCatching { Unit }
    }

    override fun getTodaysVerseOfTheDay(): Flow<VerseOfTheDay?> {
        return flowOf(verseOfTheDay)
    }
}
