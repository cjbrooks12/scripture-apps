package com.caseyjbrooks.scripturenow.config

import kotlinx.coroutines.flow.Flow

public interface ObservableRemoteConfig {
    public fun getRemoteConfig(): Flow<RemoteAppConfig>
}
