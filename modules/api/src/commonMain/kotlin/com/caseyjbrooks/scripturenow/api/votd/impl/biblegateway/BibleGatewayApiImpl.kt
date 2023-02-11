package com.caseyjbrooks.scripturenow.api.votd.impl.biblegateway

import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApi
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.utils.now
import kotlinx.datetime.LocalDate

internal class BibleGatewayApiImpl(
    private val api: BibleGatewayApi,
    private val converter: BibleGatewayApiConverter,
) : VerseOfTheDayApi {

    override suspend fun getTodaysVerseOfTheDay(): VerseOfTheDay {
        return api
            .getVerseOfTheDay()
            .let { converter.convertApiModelToRepositoryModel(LocalDate.now(), it) }
    }

    override suspend fun getHistoricalVerseOfTheDay(date: LocalDate): VerseOfTheDay {
        throw NotImplementedError("Historical verses not supported by BibleGateway")
    }

    override suspend fun getRandomVerseOfTheDay(): VerseOfTheDay {
        throw NotImplementedError("Random verses not supported by BibleGateway")
    }
}
