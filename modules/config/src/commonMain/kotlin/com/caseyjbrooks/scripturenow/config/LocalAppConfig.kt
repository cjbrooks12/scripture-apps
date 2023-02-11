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
        public fun get(): LocalAppConfig {
            return LocalAppConfigImpl()
        }
    }
}
