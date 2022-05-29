package com.copperleaf.scripturenow.verses.sql

import com.copperleaf.scripturenow.verses.VerseOfTheDay
import com.copperleaf.scripturenow.VerseOfTheDay as VerseOfTheDayRecord

class VerseOfTheDayDbConverterImpl : VerseOfTheDayDbConverter {
    override fun convertDbModelToRepositoryModel(
        dbModel: VerseOfTheDayRecord
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
