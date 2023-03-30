package com.caseyjbrooks.scripturenow.config.remote

internal data class RemoteAppConfigImpl(
    override val latestAppVersion: String,
    override val minAppVersion: String,
) : RemoteAppConfig
