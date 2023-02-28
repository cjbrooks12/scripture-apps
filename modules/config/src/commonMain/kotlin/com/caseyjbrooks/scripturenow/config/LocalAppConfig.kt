package com.caseyjbrooks.scripturenow.config

public interface LocalAppConfig {
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
    }
}
