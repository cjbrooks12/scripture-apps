package com.caseyjbrooks.prayer.repository.settings

import com.caseyjbrooks.prayer.models.PrayerTag

public class InMemoryPrayerUserSettings(
    override var dailyVerseTag: PrayerTag = PrayerTag("Daily Prayer"),
    override var addDefaultTagToSavedDailyPrayer: Boolean = true,
    override var saveTagsFromDailyPrayer: Boolean = true,
) : PrayerUserSettings
