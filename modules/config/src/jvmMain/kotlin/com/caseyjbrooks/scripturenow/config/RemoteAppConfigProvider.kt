package com.caseyjbrooks.scripturenow.config

public actual object RemoteAppConfigProvider {
    public actual suspend fun get(localAppConfig: LocalAppConfig): RemoteAppConfig {
        TODO()
    }
}
