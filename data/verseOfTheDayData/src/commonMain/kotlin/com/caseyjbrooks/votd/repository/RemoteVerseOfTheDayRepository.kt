package com.caseyjbrooks.votd.repository

import com.caseyjbrooks.votd.models.VerseOfTheDay

internal class RemoteVerseOfTheDayRepository : VerseOfTheDayRepository {
    override suspend fun getTodaysVerseOfTheDay(): VerseOfTheDay {
        TODO("Not yet implemented")
    }
}
