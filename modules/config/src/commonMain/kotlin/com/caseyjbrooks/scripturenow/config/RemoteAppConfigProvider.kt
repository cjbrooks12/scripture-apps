package com.caseyjbrooks.scripturenow.config

public expect object RemoteAppConfigProvider {
    public suspend fun get(localAppConfig: LocalAppConfig): RemoteAppConfig
}
