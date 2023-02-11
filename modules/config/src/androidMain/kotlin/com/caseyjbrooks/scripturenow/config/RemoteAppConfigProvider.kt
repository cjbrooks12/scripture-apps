package com.caseyjbrooks.scripturenow.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

public actual object RemoteAppConfigProvider {
    public actual suspend fun get(localAppConfig: LocalAppConfig): RemoteAppConfig {
        return suspendCancellableCoroutine { cont ->
            Firebase.remoteConfig.setConfigSettingsAsync(
                remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
            )

            // activate in background, and publish the update when they're available
            Firebase.remoteConfig
                .fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        cont.resume(RemoteAppConfigImpl())
                    } else {
                    }
                }
        }
    }
}
