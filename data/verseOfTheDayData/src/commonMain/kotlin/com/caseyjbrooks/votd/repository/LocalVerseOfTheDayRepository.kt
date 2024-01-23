package com.caseyjbrooks.votd.repository

import com.caseyjbrooks.votd.models.VerseOfTheDay

internal class LocalVerseOfTheDayRepository : VerseOfTheDayRepository {
    override suspend fun getTodaysVerseOfTheDay(): VerseOfTheDay {
        TODO("Not yet implemented")
    }
}
