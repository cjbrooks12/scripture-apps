package com.caseyjbrooks.scripturenow.api.votd.impl.dotcom

import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApi
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.utils.now
import kotlinx.datetime.LocalDate

internal class DotComApiImpl(
    private val api: DotComApi,
    private val converter: DotComApiConverter,
) : VerseOfTheDayApi {

    override suspend fun getTodaysVerseOfTheDay(): VerseOfTheDay {
        return api
            .getVerseOfTheDay()
            .let { converter.convertApiModelToRepositoryModel(LocalDate.now(), it) }
    }

    override suspend fun getHistoricalVerseOfTheDay(date: LocalDate): VerseOfTheDay {
        TODO()
    }

    override suspend fun getRandomVerseOfTheDay(): VerseOfTheDay {
        TODO()
    }
}
