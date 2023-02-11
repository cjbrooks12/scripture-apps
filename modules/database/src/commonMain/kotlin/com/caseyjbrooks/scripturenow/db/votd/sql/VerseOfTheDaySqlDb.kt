package com.caseyjbrooks.scripturenow.db.votd.sql

import com.caseyjbrooks.scripturenow.db.votd.VerseOfTheDayDb
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.mapIfNotNull
import com.copperleaf.scripturenow.Sn_verseOfTheDayQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

internal class VerseOfTheDaySqlDb(
    private val queries: Sn_verseOfTheDayQueries,
    private val converter: VerseOfTheDaySqlDbConverter,
) : VerseOfTheDayDb {
    override fun getCachedVerseOfTheDay(service: VerseOfTheDayService, localDate: LocalDate): Flow<VerseOfTheDay?> {
        return queries
            .getByDay(service, localDate)
            .asFlow()
            .mapToOneOrNull()
            .mapIfNotNull(converter::convertDbModelToRepositoryModel)
    }

    override suspend fun saveVerseOfTheDay(verse: VerseOfTheDay) {
        queries
            .insertOrReplace(converter.convertRepositoryModelToDbModel(verse))
    }
}
