package com.caseyjbrooks.prayer.domain.getbyid

import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayer
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.flow.Flow

/**
 * Fetch a single verse by its primary UUID, to display or interact with somewhere in the UI.
 */
public interface GetPrayerByIdUseCase {

    /**
     * Load a verse from the user's main collection by its UUID. It may be fetched from a local cache and be available
     * offline, or from a remote source which would fail if there is no internet connection.
     */
    public operator fun invoke(prayerId: PrayerId): Flow<Cached<SavedPrayer>>
}
