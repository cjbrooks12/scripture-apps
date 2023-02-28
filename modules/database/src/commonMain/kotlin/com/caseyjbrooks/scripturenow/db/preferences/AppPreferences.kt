package com.caseyjbrooks.scripturenow.db.preferences

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService

public interface AppPreferences {
    public val verseOfTheDayService: VerseOfTheDayService
    public val firebaseInstallationId: String
    public val firebaseToken: String
    public val showMainVerse: Boolean

    public companion object {
        public val Defaults: AppPreferences
            get() = AppPreferencesImpl(
                verseOfTheDayService = VerseOfTheDayService.Default,
                firebaseInstallationId = "",
                firebaseToken = "",
                showMainVerse = false,
            )
    }
}
