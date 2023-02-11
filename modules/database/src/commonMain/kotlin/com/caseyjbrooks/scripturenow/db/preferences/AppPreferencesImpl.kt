package com.caseyjbrooks.scripturenow.db.preferences

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.enumSettingOf
import com.russhwolf.settings.Settings

internal class AppPreferencesImpl(
    private val settings: Settings
) : AppPreferences, Settings by settings {
    override var verseOfTheDayService by enumSettingOf(VerseOfTheDayService.OurManna, VerseOfTheDayService::valueOf)
}
