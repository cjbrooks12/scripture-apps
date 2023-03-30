package com.caseyjbrooks.scripturenow.config.local

import io.github.copper_leaf.config.*

public interface LocalAppConfig {
    public val appVersion: String

    public val verseOfTheDayDotComBaseUrl: String
    public val bibleGatewayApiBaseUrl: String
    public val ourMannaApiBaseUrl: String
    public val theySaidSoApiBaseUrl: String

    public val logPrefix: String
    public val logApiCalls: Boolean
    public val logDbQueries: Boolean
    public val logRepositories: Boolean
    public val logViewModels: Boolean

    public companion object {
        public val Defaults: LocalAppConfig
            get() = LocalAppConfigImpl(
                appVersion = "",
                verseOfTheDayDotComBaseUrl = "",
                bibleGatewayApiBaseUrl = "",
                ourMannaApiBaseUrl = "",
                theySaidSoApiBaseUrl = "",
                logPrefix = "",
                logApiCalls = false,
                logDbQueries = false,
                logRepositories = false,
                logViewModels = false,
            )

        public val ActualDefaults: LocalAppConfig
            get() = LocalAppConfigImpl(
                appVersion = APP_VERSION,
                verseOfTheDayDotComBaseUrl = BASEURL_VERSEOFTHEDAYDOTCOM,
                bibleGatewayApiBaseUrl = BASEURL_BIBLEGATEWAY,
                ourMannaApiBaseUrl = BASEURL_OURMANNA,
                theySaidSoApiBaseUrl = BASEURL_THEYSAIDSO,
                logPrefix = LOG_PREFIX,
                logApiCalls = LOG_API_CALLS,
                logDbQueries = LOG_DB_QUERIES,
                logRepositories = LOG_REPOSITORIES,
                logViewModels = LOG_VIEWMODELS,
            )
    }
}
