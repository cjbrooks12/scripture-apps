package com.caseyjbrooks.verses.repository.settings

import com.caseyjbrooks.verses.models.VerseTag

/**
 * User-managed preferences and other settings that the user may directly or indirectly be able to change.
 */
public interface VerseUserSettings {
    /**
     * Whether the list of tags provided by the Daily Verse API should also be kept when the [DailyVerse] is saved
     * to the user's collection.
     */
    public var addDefaultTagToSavedDailyVerse: Boolean

    /**
     * The tag set by the application when saving a [DailyVerse] to identify it as such.
     */
    public var dailyVerseTag: VerseTag

    /**
     * Whether the list of tags provided by the Daily Verse API should also be kept when the [DailyVerse] is saved
     * to the user's collection.
     */
    public var saveTagsFromDailyVerse: Boolean
}
