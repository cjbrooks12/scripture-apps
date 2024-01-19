package com.caseyjbrooks.prayer.repository.daily

import com.caseyjbrooks.prayer.models.DailyPrayer
import kotlinx.coroutines.flow.Flow

/**
 * Every day a new prayer is visible to the user, as a prompt for them to pray about. This Repository wraps the API or
 * other data source providing the content for that daily prayer topic.
 */
public interface DailyPrayerRepository {

    /**
     * Return the [DailyPrayer] from the local cache if possible (for offline access), or from the remote source if the
     * cached value is missing or expired.
     *
     * Returns null if we are unable to get the Daily Prayer for any reason (such as being offline when the cache is
     * empty).
     */
    public fun getTodaysDailyPrayer(): Flow<DailyPrayer?>
}
