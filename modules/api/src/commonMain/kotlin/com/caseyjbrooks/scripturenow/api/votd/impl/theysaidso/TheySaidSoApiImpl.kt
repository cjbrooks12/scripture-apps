package com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso

import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApi
import com.caseyjbrooks.scripturenow.api.votd.impl.theysaidso.models.TheySaidSoBibleVerseResponse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.utils.converter.Converter
import com.caseyjbrooks.scripturenow.utils.now
import kotlinx.datetime.LocalDate

internal class TheySaidSoApiImpl(
    private val api: TheySaidSoApi,
    private val converter: Converter<Pair<LocalDate, TheySaidSoBibleVerseResponse>, VerseOfTheDay>,
) : VerseOfTheDayApi {

    override suspend fun getTodaysVerseOfTheDay(): VerseOfTheDay {
        return api
            .getVerseOfTheDay()
            .let { converter.convertValue(LocalDate.now() to it) }
    }

    override suspend fun getHistoricalVerseOfTheDay(date: LocalDate): VerseOfTheDay {
        throw NotImplementedError("Historical verses not supported by They Said So")
    }

    override suspend fun getRandomVerseOfTheDay(): VerseOfTheDay {
        return api
            .getRandomVerse()
            .let { converter.convertValue(LocalDate.now() to it) }
    }
}
