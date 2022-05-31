package com.copperleaf.scripturenow.db.votd.sql

import com.copperleaf.scripturenow.Sn_verseOfTheDay
import com.copperleaf.scripturenow.common.atStartOfDay
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay

class VerseOfTheDaySqlDbConverterImpl : VerseOfTheDaySqlDbConverter {

    override fun convertDbModelToRepositoryModel(
        dbModel: Sn_verseOfTheDay
    ): VerseOfTheDay = with(dbModel) {
        return VerseOfTheDay(
            uuid = uuid,
            date = date,
            text = text,
            reference = reference,
            version = version,
            verseUrl = verse_url,
            notice = notice,
        )
    }

    override fun convertRepositoryModelToDbModel(
        repositoryModel: VerseOfTheDay
    ): Sn_verseOfTheDay = with(repositoryModel) {
        return Sn_verseOfTheDay(
            uuid = uuid,
            text = text,
            reference = reference,
            version = version,
            verse_url = verseUrl,
            notice = notice,
            date = date,
            created_at = date.atStartOfDay(),
            updated_at = date.atStartOfDay(),
        )
    }
}
