package com.caseyjbrooks.scripturenow.db.prayer.sql

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.db.prayer.PrayerDb
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.caseyjbrooks.scripturenow.utils.mapEach
import com.caseyjbrooks.scripturenow.utils.mapIfNotNull
import com.copperleaf.scripturenow.Sn_prayerQueries
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow

internal class PrayerSqlDb(
    private val queries: Sn_prayerQueries,
    private val converter: PrayerSqlDbConverter,
) : PrayerDb {
    override fun getPrayers(): Flow<List<Prayer>> {
        return queries
            .getAll()
            .asFlow()
            .mapToList()
            .mapEach(converter::convertDbModelToRepositoryModel)
    }

    override fun getPrayerById(id: Uuid): Flow<Prayer?> {
        return queries
            .getById(id)
            .asFlow()
            .mapToOneOrNull()
            .mapIfNotNull(converter::convertDbModelToRepositoryModel)
    }

    override suspend fun savePrayer(prayer: Prayer) {
        queries
            .insertOrReplace(converter.convertRepositoryModelToDbModel(prayer))
    }

    override suspend fun deletePrayer(prayer: Prayer) {
        queries
            .delete(prayer.uuid)
    }
}
