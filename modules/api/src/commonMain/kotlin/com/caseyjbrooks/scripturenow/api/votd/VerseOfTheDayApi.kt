package com.caseyjbrooks.scripturenow.api.votd

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import kotlinx.datetime.LocalDate

public interface VerseOfTheDayApi {
    public suspend fun getTodaysVerseOfTheDay(): VerseOfTheDay
    public suspend fun getHistoricalVerseOfTheDay(date: LocalDate): VerseOfTheDay
    public suspend fun getRandomVerseOfTheDay(): VerseOfTheDay
}
