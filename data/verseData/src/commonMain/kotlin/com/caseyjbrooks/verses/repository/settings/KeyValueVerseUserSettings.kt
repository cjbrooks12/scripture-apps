package com.caseyjbrooks.verses.repository.settings

import com.caseyjbrooks.verses.models.VerseTag
import com.russhwolf.settings.Settings

internal class KeyValueVerseUserSettings(
    private val settings: Settings
) : VerseUserSettings {
    private companion object {
        private const val prefix = "VerseUserSettings"
        private const val dailyVerseTagKey = "$prefix.dailyVerseTag"
        private const val addDefaultTagToSavedDailyVerseKey = "$prefix.addDefaultTagToSavedDailyVerse"
        private const val saveTagsFromDailyVerseKey = "$prefix.saveTagsFromDailyVerse"
    }

    override var dailyVerseTag: VerseTag
        get() {
            return VerseTag(settings.getString(dailyVerseTagKey, "Daily Verse"))
        }
        set(value) {
            settings.putString(dailyVerseTagKey, value.tag)
        }
    override var addDefaultTagToSavedDailyVerse: Boolean
        get() {
            return settings.getBoolean(addDefaultTagToSavedDailyVerseKey, true)
        }
        set(value) {
            settings.putBoolean(addDefaultTagToSavedDailyVerseKey, value)
        }
    override var saveTagsFromDailyVerse: Boolean
        get() {
            return settings.getBoolean(saveTagsFromDailyVerseKey, true)
        }
        set(value) {
            settings.putBoolean(saveTagsFromDailyVerseKey, value)
        }
}
