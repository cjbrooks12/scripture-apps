package com.copperleaf.scripturenow.api.votd

import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay
import kotlinx.datetime.LocalDate

interface VerseOfTheDayApi {
    suspend fun getVerseOfTheDay(date: LocalDate): VerseOfTheDay
}
