package com.caseyjbrooks.scripturenow.repositories.prayer

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

public interface PrayerRepository {

    public fun getAllPrayers(refreshCache: Boolean = false): Flow<Cached<List<Prayer>>>

    public fun getPrayer(uuid: Uuid, refreshCache: Boolean = false): Flow<Cached<Prayer>>

    public suspend fun createOrUpdatePrayer(prayer: Prayer)

    public suspend fun deletePrayer(prayer: Prayer)
}
