package com.caseyjbrooks.scripturenow.db.preferences

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.BooleanSettingsProperty
import com.caseyjbrooks.scripturenow.utils.EnumSettingsProperty
import com.caseyjbrooks.scripturenow.utils.StringSettingsProperty
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow

internal class AppPreferencesImpl(
    private val settings: ObservableSettings
) : AppPreferences, Settings by settings {
    private val verseOfTheDayService = EnumSettingsProperty(
        settings,
        "verseOfTheDayService",
        VerseOfTheDayService.Default,
        VerseOfTheDayService::valueOf,
    )
    private val firebaseInstallationId = StringSettingsProperty(settings, "firebaseInstallationId")
    private val firebaseToken = StringSettingsProperty(settings, "firebaseToken")
    private val showMainVerse = BooleanSettingsProperty(settings, "showMainVerse")

    override fun getVerseOfTheDayServiceAsFlow(): Flow<VerseOfTheDayService> {
        return verseOfTheDayService.getAsFlow()
    }

    override suspend fun setVerseOfTheDayService(value: VerseOfTheDayService) {
        verseOfTheDayService.set(value)
    }

    override fun getFirebaseInstallationIdAsFlow(): Flow<String> {
        return firebaseInstallationId.getAsFlow()
    }

    override suspend fun setFirebaseInstallationId(value: String) {
        firebaseInstallationId.set(value)
    }

    override fun getFirebaseTokenAsFlow(): Flow<String> {
        return firebaseToken.getAsFlow()
    }

    override suspend fun setFirebaseToken(value: String) {
        firebaseToken.set(value)
    }

    override fun getShowMainVerse(): Flow<Boolean> {
        return showMainVerse.getAsFlow()
    }

    override suspend fun setShowMainVerse(value: Boolean) {
        showMainVerse.set(value)
    }
}
