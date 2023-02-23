package com.caseyjbrooks.scripturenow.db.preferences

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import kotlinx.coroutines.flow.Flow

public interface AppPreferences {
    public fun getVerseOfTheDayServiceAsFlow(): Flow<VerseOfTheDayService>
    public suspend fun setVerseOfTheDayService(value: VerseOfTheDayService)

    public fun getFirebaseInstallationIdAsFlow(): Flow<String>
    public suspend fun setFirebaseInstallationId(value: String)

    public fun getFirebaseTokenAsFlow(): Flow<String>
    public suspend fun setFirebaseToken(value: String)

    public fun getShowMainVerse(): Flow<Boolean>
    public suspend fun setShowMainVerse(value: Boolean)
}
