package com.caseyjbrooks.api

import io.ktor.client.plugins.logging.Logger
import co.touchlab.kermit.Logger as KermitLogger

internal class KtorKermitLogger(
    private val logger: KermitLogger
) : Logger {
    override fun log(message: String) {
        logger.i { message }
    }
}
