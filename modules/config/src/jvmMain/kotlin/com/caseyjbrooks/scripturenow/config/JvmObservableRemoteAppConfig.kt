package com.caseyjbrooks.scripturenow.config

import com.caseyjbrooks.scripturenow.config.remote.ObservableRemoteConfig
import com.caseyjbrooks.scripturenow.config.remote.RemoteAppConfig
import kotlinx.coroutines.flow.Flow

public class JvmObservableRemoteAppConfig : ObservableRemoteConfig {

    override suspend fun checkForUpdates(force: Boolean) {
        TODO()
    }

    override fun getRemoteConfig(): Flow<RemoteAppConfig> {
        TODO()
    }
}
