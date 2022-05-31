package com.copperleaf.scripturenow.db.votd.sql

import com.benasher44.uuid.uuid4
import com.copperleaf.scripturenow.Sn_verseOfTheDayQueries
import com.copperleaf.scripturenow.common.atStartOfDay
import com.copperleaf.scripturenow.db.votd.VerseOfTheDayDb
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            .map { if(it != null) converter.convertDbModelToRepositoryModel(it) else null }
    }

    override suspend fun saveVerseOfTheDay(verse: VerseOfTheDay) {
        queries.insert(
            uuid = uuid4(),
            text = verse.text,
            reference = verse.reference,
            version = verse.version,
            verse_url = verse.verseUrl,
            notice = verse.notice,
            date = verse.date,
            created_at = verse.date.atStartOfDay(),
            updated_at = verse.date.atStartOfDay(),
        )
    }
}
