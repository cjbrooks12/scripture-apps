package com.copperleaf.scripturenow.verses.sql

import com.copperleaf.scripturenow.VerseOfTheDayQueries
import com.copperleaf.scripturenow.common.atStartOfDay
import com.copperleaf.scripturenow.verses.VerseOfTheDay
import com.copperleaf.scripturenow.verses.VerseOfTheDayDb
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class VerseOfTheDayDbImpl(
    private val queries: VerseOfTheDayQueries,
    private val converter: VerseOfTheDayDbConverter,
) : VerseOfTheDayDb {
    override fun getVerseOfTheDay(localDate: LocalDate): Flow<VerseOfTheDay?> {
        return queries
            .getByDay(localDate)
            .asFlow()
            .mapToOneOrNull()
            .map { if(it != null) converter.convertDbModelToRepositoryModel(it) else null }
    }

    override suspend fun saveVerseOfTheDat(verse: VerseOfTheDay) {
        queries.insert(
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
