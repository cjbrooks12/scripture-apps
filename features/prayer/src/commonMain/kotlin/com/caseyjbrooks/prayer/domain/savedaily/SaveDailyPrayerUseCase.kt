package com.caseyjbrooks.prayer.domain.savedaily

import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.SavedPrayer

/**
 * The user may choose to save the current [DailyPrayer] to their personal collection.
 */
public interface SaveDailyPrayerUseCase {

    /**
     * Save the current [DailyPrayer] to the user's collection, if it is not already saved. If the prayer has already
     * been saved, this operation is just a no-op.
     *
     * The resulting [SavedPrayer] is returned, whether it was newly saved or not.
     */
    public suspend operator fun invoke(): SavedPrayer
}
