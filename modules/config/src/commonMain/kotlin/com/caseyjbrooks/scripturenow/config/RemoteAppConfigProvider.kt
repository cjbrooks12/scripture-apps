package com.caseyjbrooks.scripturenow.config

public interface RemoteAppConfigProvider {
    public fun getRemoteConfig(localAppConfig: LocalAppConfig): ObservableRemoteConfig
}
