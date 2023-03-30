package com.caseyjbrooks.scripturenow.repositories.global

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import kotlinx.coroutines.flow.StateFlow

public interface GlobalRepository {

    public fun getGlobalState(): StateFlow<GlobalRepositoryContract.State>

    public fun signOut()

    public fun setVerseOfTheDayService(value: VerseOfTheDayService)
    public fun setFirebaseInstallationId(value: String)
    public fun setFirebaseToken(value: String)
    public fun setShowMainVerse(value: Boolean)

    public fun checkForUpdates()
}
