package com.caseyjbrooks.scripturenow.config

public interface RemoteAppConfig {
    public companion object {
        public val Defaults: RemoteAppConfig get() = RemoteAppConfigImpl()
    }
}
