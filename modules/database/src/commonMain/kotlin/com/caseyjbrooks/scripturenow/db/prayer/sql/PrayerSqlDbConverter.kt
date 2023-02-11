package com.caseyjbrooks.scripturenow.db.prayer.sql

import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.scripturenow.Sn_prayer

internal interface PrayerSqlDbConverter {

    fun convertDbModelToRepositoryModel(
        dbModel: Sn_prayer
    ): Prayer

    fun convertRepositoryModelToDbModel(
        repositoryModel: Prayer
    ): Sn_prayer
}
