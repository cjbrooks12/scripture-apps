package com.caseyjbrooks.votd.repository

import com.caseyjbrooks.votd.models.VerseOfTheDay

internal class FakeVerseOfTheDayRepository(
    private val verseOfTheDay: VerseOfTheDay
) : VerseOfTheDayRepository {
    override suspend fun getTodaysVerseOfTheDay(): VerseOfTheDay {
        return verseOfTheDay
    }
}
