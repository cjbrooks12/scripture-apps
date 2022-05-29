package com.copperleaf.scripturenow.db.votd.sql

import com.copperleaf.scripturenow.Sn_verseOfTheDay
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay

interface VerseOfTheDaySqlDbConverter {

    fun convertDbModelToRepositoryModel(
        dbModel: Sn_verseOfTheDay
    ) : VerseOfTheDay
}
