package com.copperleaf.scripturenow.votd

import kotlinx.datetime.LocalDate

interface VerseOfTheDayApi {
    suspend fun getVerseOfTheDay(date: LocalDate): VerseOfTheDay
}
