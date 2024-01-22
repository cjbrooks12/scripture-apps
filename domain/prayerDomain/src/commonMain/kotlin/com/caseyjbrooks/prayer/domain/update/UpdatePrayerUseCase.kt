package com.caseyjbrooks.prayer.domain.update

import com.caseyjbrooks.prayer.models.SavedPrayer

/**
 * The user may make edits to their prayers. This may be the result of the user directly editing the prayer, or indirect
 * changes made as a result of other user-interactions or system processing.
 *
 * Create the [savedPrayer] if it is not yet saved in the user's collection, otherwise update the existing record.
 *
 * The [SavedPrayer.updatedAt] timestamp will be "touched" to denote the moment this change was saved.
 */
public interface UpdatePrayerUseCase {
    public suspend operator fun invoke(prayer: SavedPrayer): SavedPrayer
}
