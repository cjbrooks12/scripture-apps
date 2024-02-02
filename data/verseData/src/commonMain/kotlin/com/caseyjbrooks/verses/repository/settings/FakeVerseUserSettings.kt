package com.caseyjbrooks.verses.repository.settings

import com.caseyjbrooks.verses.models.VerseTag

internal class FakeVerseUserSettings(
    override var dailyVerseTag: VerseTag = VerseTag("Daily Verse"),
    override var addDefaultTagToSavedDailyVerse: Boolean = true,
    override var saveTagsFromDailyVerse: Boolean = true,
) : VerseUserSettings
