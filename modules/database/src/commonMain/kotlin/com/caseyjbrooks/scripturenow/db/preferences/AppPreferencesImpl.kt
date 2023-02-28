package com.caseyjbrooks.scripturenow.db.preferences

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService

internal data class AppPreferencesImpl(
    override val verseOfTheDayService: VerseOfTheDayService,
    override val firebaseInstallationId: String,
    override val firebaseToken: String,
    override val showMainVerse: Boolean,
) : AppPreferences
