package com.caseyjbrooks.scripturenow.api.votd.impl.ourmanna

import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApi
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.utils.now
import kotlinx.datetime.LocalDate

internal class OurMannaApiImpl(
    private val api: OurMannaApi,
    private val converter: OurMannaApiConverter,
) : VerseOfTheDayApi {

    override suspend fun getTodaysVerseOfTheDay(): VerseOfTheDay {
        return api
            .getVerseOfTheDay(order = OurMannaApi.VerseOfTheDayOrder.daily.name)
            .let { converter.convertApiModelToRepositoryModel(LocalDate.now(), it) }
    }

    override suspend fun getHistoricalVerseOfTheDay(date: LocalDate): VerseOfTheDay {
        throw NotImplementedError("Historical verses not supported by OurManna")
    }

    override suspend fun getRandomVerseOfTheDay(): VerseOfTheDay {
        return api
            .getVerseOfTheDay(order = OurMannaApi.VerseOfTheDayOrder.random.name)
            .let { converter.convertApiModelToRepositoryModel(LocalDate.now(), it) }
    }
}
