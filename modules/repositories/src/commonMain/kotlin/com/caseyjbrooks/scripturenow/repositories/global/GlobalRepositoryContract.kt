package com.caseyjbrooks.scripturenow.repositories.global

import com.caseyjbrooks.scripturenow.config.local.LocalAppConfig
import com.caseyjbrooks.scripturenow.config.remote.RemoteAppConfig
import com.caseyjbrooks.scripturenow.db.preferences.AppPreferences
import com.caseyjbrooks.scripturenow.models.auth.AuthState
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService

public object GlobalRepositoryContract {
    public data class State(
        val localConfig: LocalAppConfig = LocalAppConfig.Defaults,
        val remoteAppConfig: RemoteAppConfig = RemoteAppConfig.Defaults,
        val appPreferences: AppPreferences = AppPreferences.Defaults,
        val authState: AuthState = AuthState.SignedOut,
    )

    public sealed class Inputs {
        public object Initialize : Inputs()
        public data class AppPreferencesUpdated(val appPreferences: AppPreferences) : Inputs()
        public data class RemoteConfigUpdated(val remoteAppConfig: RemoteAppConfig) : Inputs()
        public data class AuthStateUpdated(val authState: AuthState) : Inputs()

        public object RequestSignOut : Inputs()
        public object CheckForUpdates : Inputs()

        public data class UpdateVerseOfTheDayService(val value: VerseOfTheDayService) : Inputs()
        public data class UpdateFirebaseInstallationId(val value: String) : Inputs()
        public data class UpdateFirebaseToken(val value: String) : Inputs()
        public data class UpdateShowMainVerse(val value: Boolean) : Inputs()
    }

    public sealed class Events {
        public object NavigateUp : Events()
    }
}
