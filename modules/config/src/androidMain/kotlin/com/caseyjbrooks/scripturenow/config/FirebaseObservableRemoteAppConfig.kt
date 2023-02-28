package com.caseyjbrooks.scripturenow.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseObservableRemoteAppConfig : ObservableRemoteConfig {
    override fun getRemoteConfig(): Flow<RemoteAppConfig> {
        return callbackFlow {
            // eagerly send the initial value with hardcoded defaults
            trySend(RemoteAppConfig.Defaults)

            Firebase.remoteConfig.setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
            )

            // activate in background, and publish the update when they're available
            val handle = Firebase.remoteConfig
                .fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        trySend(RemoteAppConfigImpl())
                    } else {
                    }
                }

            awaitClose {
                // TODO: how ot unregister from the fetch task
            }
        }
    }
}
