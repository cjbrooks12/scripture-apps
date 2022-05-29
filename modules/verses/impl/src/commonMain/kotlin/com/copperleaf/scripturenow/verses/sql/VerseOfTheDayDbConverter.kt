package com.copperleaf.scripturenow.verses.sql

import com.copperleaf.scripturenow.verses.VerseOfTheDay
import com.copperleaf.scripturenow.VerseOfTheDay as VerseOfTheDayRecord

interface VerseOfTheDayDbConverter {

    fun convertDbModelToRepositoryModel(
        dbModel: VerseOfTheDayRecord
    ) : VerseOfTheDay
}
