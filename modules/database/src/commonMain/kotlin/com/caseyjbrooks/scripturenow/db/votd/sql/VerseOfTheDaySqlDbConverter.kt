package com.caseyjbrooks.scripturenow.db.votd.sql

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.copperleaf.scripturenow.Sn_verseOfTheDay

internal interface VerseOfTheDaySqlDbConverter {

    fun convertDbModelToRepositoryModel(
        dbModel: Sn_verseOfTheDay
    ): VerseOfTheDay

    fun convertRepositoryModelToDbModel(
        repositoryModel: VerseOfTheDay
    ): Sn_verseOfTheDay
}
