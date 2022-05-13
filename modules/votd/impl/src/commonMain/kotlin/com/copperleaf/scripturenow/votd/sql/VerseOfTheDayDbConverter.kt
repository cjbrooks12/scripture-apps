package com.copperleaf.scripturenow.votd.sql

import com.copperleaf.scripturenow.votd.VerseOfTheDay
import com.copperleaf.scripturenow.VerseOfTheDay as VerseOfTheDayRecord

interface VerseOfTheDayDbConverter {

    fun convertDbModelToRepositoryModel(
        dbModel: VerseOfTheDayRecord
    ) : VerseOfTheDay
}
