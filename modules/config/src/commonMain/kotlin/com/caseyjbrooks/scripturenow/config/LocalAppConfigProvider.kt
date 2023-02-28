package com.caseyjbrooks.scripturenow.config

public interface LocalAppConfigProvider {
    public fun getLocalAppConfig(): LocalAppConfig {
        return LocalAppConfigImpl()
    }
}
