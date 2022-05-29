package com.copperleaf.scripturenow.verses

import kotlinx.datetime.LocalDate

interface VerseOfTheDayApi {
    suspend fun getVerseOfTheDay(date: LocalDate): VerseOfTheDay
}
