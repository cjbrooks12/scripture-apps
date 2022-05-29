package com.copperleaf.scripturenow.api.votd.ourmanna

import com.copperleaf.scripturenow.api.votd.VerseOfTheDayApi
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay
import kotlinx.datetime.LocalDate

class OurMannaApiImpl(
    private val api: OurMannaApi,
    private val converter: OurMannaApiConverter,
) : VerseOfTheDayApi {

    override suspend fun getVerseOfTheDay(date: LocalDate): VerseOfTheDay {
        return api
            .getVerseOfTheDay()
            .let { converter.convertApiModelToRepositoryModel(date, it) }
    }
}
