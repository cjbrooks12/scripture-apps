package com.caseyjbrooks.scripturenow.config.remote

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class FirebaseObservableRemoteAppConfig : ObservableRemoteConfig {

    private val backingConfig = MutableStateFlow<RemoteAppConfig>(RemoteAppConfig.Defaults)

    override suspend fun checkForUpdates(force: Boolean) {
        val isSuccessful = suspendCancellableCoroutine { cont ->
            Firebase.remoteConfig.setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = if (force) 0 else 3600
                }
            )

            // activate in background, and publish the update when they're available
            Firebase.remoteConfig
                .fetchAndActivate()
                .addOnCompleteListener { task ->
                    cont.resume(task.isSuccessful)
                }
        }

        if (isSuccessful) {
            backingConfig.update {
                RemoteAppConfig.fromFirebase()
            }
        }
    }

    override fun getRemoteConfig(): Flow<RemoteAppConfig> {
        return backingConfig
            .onSubscription { checkForUpdates(force = false) }
    }

    private fun RemoteAppConfig.Companion.fromFirebase(): RemoteAppConfig {
        return with(Firebase.remoteConfig) {
            RemoteAppConfigImpl(
                latestAppVersion = getString("latest_app_version"),
                minAppVersion = getString("min_app_version"),
            )
        }
    }
}
