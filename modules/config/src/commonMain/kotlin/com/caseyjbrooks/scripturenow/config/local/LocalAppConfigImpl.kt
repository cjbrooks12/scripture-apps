package com.caseyjbrooks.scripturenow.config.local

internal data class LocalAppConfigImpl(
    override val appVersion: String,

    override val verseOfTheDayDotComBaseUrl: String,
    override val bibleGatewayApiBaseUrl: String,
    override val ourMannaApiBaseUrl: String,
    override val theySaidSoApiBaseUrl: String,
    override val logPrefix: String,
    override val logApiCalls: Boolean,
    override val logDbQueries: Boolean,
    override val logRepositories: Boolean,
    override val logViewModels: Boolean,
) : LocalAppConfig
