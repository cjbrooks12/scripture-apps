package com.caseyjbrooks.scripturenow.db.preferences

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService

public interface AppPreferences {
    public var verseOfTheDayService: VerseOfTheDayService
    public var firebaseInstallationId: String
    public var firebaseToken: String
}
