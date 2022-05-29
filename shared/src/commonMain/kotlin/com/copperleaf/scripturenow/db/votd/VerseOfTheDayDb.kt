package com.copperleaf.scripturenow.db.votd

import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface VerseOfTheDayDb {
    fun getVerseOfTheDay(localDate: LocalDate): Flow<VerseOfTheDay?>
    suspend fun saveVerseOfTheDay(verse: VerseOfTheDay)
}
