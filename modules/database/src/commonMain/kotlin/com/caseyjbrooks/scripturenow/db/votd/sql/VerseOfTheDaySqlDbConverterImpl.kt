package com.caseyjbrooks.scripturenow.db.votd.sql

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.utils.atStartOfDay
import com.caseyjbrooks.scripturenow.utils.parseVerseReference
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.copperleaf.scripturenow.Sn_verseOfTheDay

internal class VerseOfTheDaySqlDbConverterImpl : VerseOfTheDaySqlDbConverter {

    override fun convertDbModelToRepositoryModel(
        dbModel: Sn_verseOfTheDay
    ): VerseOfTheDay = with(dbModel) {
        return VerseOfTheDay(
            uuid = uuid,
            providedBy = providedBy,
            date = date,
            text = text,
            reference = reference.parseVerseReference(),
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
            providedBy = providedBy,
            text = text,
            reference = reference.referenceText,
            version = version,
            verse_url = verseUrl,
            notice = notice,
            date = date,
            created_at = date.atStartOfDay(),
            updated_at = date.atStartOfDay(),
        )
    }
}
