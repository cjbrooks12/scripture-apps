package com.caseyjbrooks.scripturenow.config

import com.caseyjbrooks.scripturenow.config.local.LocalAppConfigProvider
import com.caseyjbrooks.scripturenow.config.remote.RemoteAppConfigProvider

public interface ConfigModuleProvider :
    LocalAppConfigProvider,
    RemoteAppConfigProvider
