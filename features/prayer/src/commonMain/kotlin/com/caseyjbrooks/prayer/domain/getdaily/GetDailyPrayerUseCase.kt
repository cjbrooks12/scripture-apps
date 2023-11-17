package com.caseyjbrooks.prayer.domain.getdaily

import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.domain.savedaily.SaveDailyPrayerUseCase
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

/**
 * Fetch today's Daily Prayer, to the display in the UI. The user may choose to save this prayer to their personal
 * collection, which is handled by the [SaveDailyPrayerUseCase].
 */
public interface GetDailyPrayerUseCase {

    /**
     * Fetch today's Daily Prayer. It may be fetched from a local cache and be available offline, or from a remote
     * source which would fail if there is no internet connection.
     */
    public operator fun invoke(): Flow<Cached<DailyPrayer>>
}
