package com.caseyjbrooks.scripturenow.db.preferences

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.BooleanSettingsProperty
import com.caseyjbrooks.scripturenow.utils.EnumSettingsProperty
import com.caseyjbrooks.scripturenow.utils.StringSettingsProperty
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

public class ObservableSettingsAppPreferences(
    private val settings: ObservableSettings
) : Settings by settings {
    private val verseOfTheDayService = EnumSettingsProperty(
        settings,
        "verseOfTheDayService",
        VerseOfTheDayService.Default,
        VerseOfTheDayService::valueOf,
    )
    private val firebaseInstallationId = StringSettingsProperty(settings, "firebaseInstallationId")
    private val firebaseToken = StringSettingsProperty(settings, "firebaseToken")
    private val showMainVerse = BooleanSettingsProperty(settings, "showMainVerse")

    public suspend fun setVerseOfTheDayService(value: VerseOfTheDayService) {
        verseOfTheDayService.set(value)
    }

    public suspend fun setFirebaseInstallationId(value: String) {
        firebaseInstallationId.set(value)
    }

    public suspend fun setFirebaseToken(value: String) {
        firebaseToken.set(value)
    }

    public suspend fun setShowMainVerse(value: Boolean) {
        showMainVerse.set(value)
    }

    public fun getAppPreferences(): Flow<AppPreferences> {
        return combine(
            verseOfTheDayService.getAsFlow(),
            firebaseInstallationId.getAsFlow(),
            firebaseToken.getAsFlow(),
            showMainVerse.getAsFlow(),
            ::AppPreferencesImpl
        )
    }
}
