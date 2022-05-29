package com.copperleaf.scripturenow.db.votd.sql

import com.copperleaf.scripturenow.Sn_verseOfTheDay
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay

class VerseOfTheDaySqlDbConverterImpl : VerseOfTheDaySqlDbConverter {

    override fun convertDbModelToRepositoryModel(
        dbModel: Sn_verseOfTheDay
    ): VerseOfTheDay = with(dbModel) {
        return VerseOfTheDay(
            date = dbModel.date,
            text = text,
            reference = reference,
            version = version,
            verseUrl = verse_url,
            notice = notice,
        )
    }
}
