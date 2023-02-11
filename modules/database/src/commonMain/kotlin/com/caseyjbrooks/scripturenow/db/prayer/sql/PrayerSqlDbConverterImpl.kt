package com.caseyjbrooks.scripturenow.db.prayer.sql

import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.caseyjbrooks.scripturenow.utils.now
import com.copperleaf.scripturenow.Sn_prayer
import kotlinx.datetime.LocalDateTime

internal class PrayerSqlDbConverterImpl : PrayerSqlDbConverter {

    override fun convertDbModelToRepositoryModel(
        dbModel: Sn_prayer
    ): Prayer = with(dbModel) {
        Prayer(
            uuid = dbModel.uuid,
            text = dbModel.text,
            createdAt = dbModel.created_at,
            updatedAt = dbModel.updated_at,
        )
    }

    override fun convertRepositoryModelToDbModel(
        repositoryModel: Prayer
    ): Sn_prayer = with(repositoryModel) {
        Sn_prayer(
            uuid = repositoryModel.uuid,
            text = repositoryModel.text,
            created_at = repositoryModel.createdAt,
            updated_at = LocalDateTime.now(),
        )
    }
}
