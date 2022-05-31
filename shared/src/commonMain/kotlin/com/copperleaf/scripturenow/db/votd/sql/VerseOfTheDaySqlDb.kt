package com.copperleaf.scripturenow.db.votd.sql

import com.copperleaf.scripturenow.Sn_verseOfTheDayQueries
import com.copperleaf.scripturenow.db.votd.VerseOfTheDayDb
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay
import com.copperleaf.scripturenow.utils.mapIfNotNull
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class VerseOfTheDaySqlDb(
    private val queries: Sn_verseOfTheDayQueries,
    private val converter: VerseOfTheDaySqlDbConverter,
) : VerseOfTheDayDb {
    override fun getVerseOfTheDay(localDate: LocalDate): Flow<VerseOfTheDay?> {
        return queries
            .getByDay(localDate)
            .asFlow()
            .mapToOneOrNull()
            .mapIfNotNull(converter::convertDbModelToRepositoryModel)
    }

    override suspend fun saveVerseOfTheDay(verse: VerseOfTheDay) {
        queries
            .insertOrReplace(converter.convertRepositoryModelToDbModel(verse))
    }
}
