package com.caseyjbrooks.scripturenow.config.remote

public interface RemoteAppConfigProvider {
    public fun getRemoteConfig(): ObservableRemoteConfig
}
