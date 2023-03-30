package com.caseyjbrooks.app.di

import com.caseyjbrooks.scripturenow.config.ConfigModuleProvider
import com.caseyjbrooks.scripturenow.config.local.LocalAppConfig
import com.caseyjbrooks.scripturenow.config.remote.FirebaseObservableRemoteAppConfig
import com.caseyjbrooks.scripturenow.config.remote.ObservableRemoteConfig

class ConfigModuleProviderImpl : ConfigModuleProvider {

    private val localConfig = LocalAppConfig.ActualDefaults
    private val remoteConfig = FirebaseObservableRemoteAppConfig()

    override fun getLocalAppConfig(): LocalAppConfig {
        return localConfig
    }

    override fun getRemoteConfig(): ObservableRemoteConfig {
        return remoteConfig
    }
}
