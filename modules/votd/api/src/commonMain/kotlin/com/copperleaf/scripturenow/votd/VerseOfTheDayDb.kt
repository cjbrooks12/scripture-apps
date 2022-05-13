package com.copperleaf.scripturenow.votd

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface VerseOfTheDayDb {
    fun getVerseOfTheDay(localDate: LocalDate): Flow<VerseOfTheDay?>
    suspend fun saveVerseOfTheDat(verse: VerseOfTheDay)
}
