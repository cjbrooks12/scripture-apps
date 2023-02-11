package com.caseyjbrooks.scripturenow.config

import io.github.copper_leaf.config.*

internal data class LocalAppConfigImpl(
    override val verseOfTheDayDotComBaseUrl: String = BASEURL_VERSEOFTHEDAYDOTCOM,
    override val bibleGatewayApiBaseUrl: String = BASEURL_BIBLEGATEWAY,
    override val ourMannaApiBaseUrl: String = BASEURL_OURMANNA,
    override val theySaidSoApiBaseUrl: String = BASEURL_THEYSAIDSO,
    override val logPrefix: String = LOG_PREFIX,
    override val logApiCalls: Boolean = LOG_API_CALLS,
    override val logDbQueries: Boolean = LOG_DB_QUERIES,
    override val logRepositories: Boolean = LOG_REPOSITORIES,
    override val logViewModels: Boolean = LOG_VIEWMODELS,
) : LocalAppConfig
