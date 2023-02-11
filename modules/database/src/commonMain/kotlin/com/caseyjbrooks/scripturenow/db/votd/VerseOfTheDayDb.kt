package com.caseyjbrooks.scripturenow.db.votd

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

public interface VerseOfTheDayDb {
    public fun getCachedVerseOfTheDay(service: VerseOfTheDayService, localDate: LocalDate): Flow<VerseOfTheDay?>
    public suspend fun saveVerseOfTheDay(verse: VerseOfTheDay)
}
