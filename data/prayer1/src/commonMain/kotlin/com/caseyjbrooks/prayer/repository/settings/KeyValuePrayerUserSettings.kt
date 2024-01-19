package com.caseyjbrooks.prayer.repository.settings

import com.caseyjbrooks.prayer.models.PrayerTag
import com.russhwolf.settings.Settings

internal class KeyValuePrayerUserSettings(
    private val settings: Settings
) : PrayerUserSettings {
    private companion object {
        private const val prefix = "PrayerUserSettings"
        private const val dailyVerseTagKey = "$prefix.dailyVerseTag"
        private const val addDefaultTagToSavedDailyPrayerKey = "$prefix.addDefaultTagToSavedDailyPrayer"
        private const val saveTagsFromDailyPrayerKey = "$prefix.saveTagsFromDailyPrayer"
    }

    override var dailyVerseTag: PrayerTag
        get() {
            return PrayerTag(settings.getString(dailyVerseTagKey, "Daily Prayer"))
        }
        set(value) {
            settings.putString(dailyVerseTagKey, value.tag)
        }
    override var addDefaultTagToSavedDailyPrayer: Boolean
        get() {
            return settings.getBoolean(addDefaultTagToSavedDailyPrayerKey, true)
        }
        set(value) {
            settings.putBoolean(addDefaultTagToSavedDailyPrayerKey, value)
        }
    override var saveTagsFromDailyPrayer: Boolean
        get() {
            return settings.getBoolean(saveTagsFromDailyPrayerKey, true)
        }
        set(value) {
            settings.putBoolean(saveTagsFromDailyPrayerKey, value)
        }
}
