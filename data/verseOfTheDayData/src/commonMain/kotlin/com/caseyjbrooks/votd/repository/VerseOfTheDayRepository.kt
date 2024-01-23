package com.caseyjbrooks.votd.repository

import com.caseyjbrooks.votd.models.VerseOfTheDay

public interface VerseOfTheDayRepository {
    public suspend fun getTodaysVerseOfTheDay(): VerseOfTheDay
}
