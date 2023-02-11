package com.caseyjbrooks.scripturenow.db.prayer

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import kotlinx.coroutines.flow.Flow

public interface PrayerDb {
    public fun getPrayers(): Flow<List<Prayer>>
    public fun getPrayerById(id: Uuid): Flow<Prayer?>
    public suspend fun savePrayer(prayer: Prayer)
    public suspend fun deletePrayer(prayer: Prayer)
}
