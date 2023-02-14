package com.caseyjbrooks.scripturenow.repositories.prayer

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.coroutines.flow.Flow

public interface PrayerRepository {

    public fun getAllPrayers(refreshCache: Boolean = false): Flow<Cached<List<Prayer>>>
    public fun getPrayerById(uuid: Uuid, refreshCache: Boolean = false): Flow<Cached<Prayer>>
    public fun loadForm(): Flow<Cached<Pair<JsonSchema, UiSchema>>>

    public suspend fun createOrUpdatePrayer(prayer: Prayer)
    public suspend fun deletePrayer(prayer: Prayer)
}
