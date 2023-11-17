package com.caseyjbrooks.prayer.repository.settings

import com.caseyjbrooks.prayer.models.DailyPrayer
import com.caseyjbrooks.prayer.models.PrayerTag

/**
 * User-managed preferences and other settings that the user may directly or indirectly be able to change.
 */
public interface PrayerUserSettings {
    /**
     * Whether the list of tags provided by the Daily Prayer API should also be kept when the [DailyPrayer] is saved
     * to the user's collection.
     */
    public var addDefaultTagToSavedDailyPrayer: Boolean

    /**
     * The tag set by the application when saving a [DailyPrayer] to identify it as such.
     */
    public var dailyVerseTag: PrayerTag

    /**
     * Whether the list of tags provided by the Daily Prayer API should also be kept when the [DailyPrayer] is saved
     * to the user's collection.
     */
    public var saveTagsFromDailyPrayer: Boolean
}
