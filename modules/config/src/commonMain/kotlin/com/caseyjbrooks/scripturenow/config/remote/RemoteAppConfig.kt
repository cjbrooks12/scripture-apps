package com.caseyjbrooks.scripturenow.config.remote

public interface RemoteAppConfig {

    public val latestAppVersion: String
    public val minAppVersion: String

    public companion object {
        public val Defaults: RemoteAppConfig get() = RemoteAppConfigImpl(
            latestAppVersion = "",
            minAppVersion = "",
        )
    }
}
