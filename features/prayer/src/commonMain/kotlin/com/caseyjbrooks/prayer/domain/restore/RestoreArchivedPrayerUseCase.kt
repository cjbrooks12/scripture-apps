package com.caseyjbrooks.prayer.domain.restore

import com.caseyjbrooks.prayer.models.SavedPrayer

/**
 * The user can move a prayer to the "archive" so it's not normally displayed in their list of current prayers. They
 * may also view their archives, and move a prayer back to their main collection.
 */
public interface RestoreArchivedPrayerUseCase {
    /**
     * Remove the [prayer] from the user's archives, putting it back into their main collection. This operation should
     * succeed even if the user is offline, as the DB will be synced back to the remote Source-Of-Truth once they're
     * back online.
     */
    public suspend operator fun invoke(prayer: SavedPrayer)
}
