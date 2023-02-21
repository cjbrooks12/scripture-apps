package com.caseyjbrooks.scripturenow.db.preferences

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.enumSettingOf
import com.caseyjbrooks.scripturenow.utils.stringSetting
import com.russhwolf.settings.Settings

internal class AppPreferencesImpl(
    private val settings: Settings
) : AppPreferences, Settings by settings {
    override var verseOfTheDayService by enumSettingOf(
        VerseOfTheDayService.OurManna,
        VerseOfTheDayService::valueOf,
    )
    override var firebaseInstallationId by stringSetting("")
    override var firebaseToken by stringSetting("")
}
