package com.caseyjbrooks.scripturenow.config.remote

import kotlinx.coroutines.flow.Flow

public interface ObservableRemoteConfig {
    public suspend fun checkForUpdates(force: Boolean)

    public fun getRemoteConfig(): Flow<RemoteAppConfig>
}
