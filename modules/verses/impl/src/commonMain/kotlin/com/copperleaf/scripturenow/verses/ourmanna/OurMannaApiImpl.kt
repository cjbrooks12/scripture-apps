package com.copperleaf.scripturenow.verses.ourmanna

import com.copperleaf.scripturenow.verses.VerseOfTheDay
import com.copperleaf.scripturenow.verses.VerseOfTheDayApi
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
