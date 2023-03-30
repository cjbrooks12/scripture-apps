package com.caseyjbrooks.scripturenow.repositories.global

import com.caseyjbrooks.scripturenow.api.auth.Session
import com.caseyjbrooks.scripturenow.config.remote.ObservableRemoteConfig
import com.caseyjbrooks.scripturenow.db.preferences.ObservableSettingsAppPreferences
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

public class GlobalRepositoryInputHandler(
    private val observableAppPreferences: ObservableSettingsAppPreferences,
    private val observableRemoteConfig: ObservableRemoteConfig,
    private val session: Session,
) : InputHandler<
        GlobalRepositoryContract.Inputs,
        GlobalRepositoryContract.Events,
        GlobalRepositoryContract.State> {
    override suspend fun InputHandlerScope<
            GlobalRepositoryContract.Inputs,
            GlobalRepositoryContract.Events,
            GlobalRepositoryContract.State>.handleInput(
        input: GlobalRepositoryContract.Inputs
    ): Unit = when (input) {
        is GlobalRepositoryContract.Inputs.Initialize -> {
            observeFlows(
                "data",
                observableAppPreferences
                    .getAppPreferences()
                    .map { GlobalRepositoryContract.Inputs.AppPreferencesUpdated(it) },
                observableRemoteConfig
                    .getRemoteConfig()
                    .map { GlobalRepositoryContract.Inputs.RemoteConfigUpdated(it) },
                session
                    .getAuthState()
                    .map { GlobalRepositoryContract.Inputs.AuthStateUpdated(it) }
            )
        }

        is GlobalRepositoryContract.Inputs.AppPreferencesUpdated -> {
            updateState { it.copy(appPreferences = input.appPreferences) }
        }

        is GlobalRepositoryContract.Inputs.RemoteConfigUpdated -> {
            updateState { it.copy(remoteAppConfig = input.remoteAppConfig) }
        }

        is GlobalRepositoryContract.Inputs.AuthStateUpdated -> {
            updateState { it.copy(authState = input.authState) }
        }

        is GlobalRepositoryContract.Inputs.UpdateVerseOfTheDayService -> {
            sideJob("UpdateVerseOfTheDayService") {
                observableAppPreferences.setVerseOfTheDayService(input.value)
            }
        }

        is GlobalRepositoryContract.Inputs.UpdateFirebaseInstallationId -> {
            sideJob("UpdateFirebaseInstallationId") {
                observableAppPreferences.setFirebaseInstallationId(input.value)
            }
        }

        is GlobalRepositoryContract.Inputs.UpdateFirebaseToken -> {
            sideJob("UpdateFirebaseToken") {
                observableAppPreferences.setFirebaseToken(input.value)
            }
        }

        is GlobalRepositoryContract.Inputs.UpdateShowMainVerse -> {
            sideJob("UpdateShowMainVerse") {
                observableAppPreferences.setShowMainVerse(input.value)
            }
        }

        is GlobalRepositoryContract.Inputs.RequestSignOut -> {
            sideJob("RequestSignOut") {
                session.signOut()
            }
        }

        is GlobalRepositoryContract.Inputs.CheckForUpdates -> {
            sideJob("CheckForUpdates") {
                observableRemoteConfig.checkForUpdates(force = true)
            }
        }
    }
}
