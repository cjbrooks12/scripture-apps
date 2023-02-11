package com.caseyjbrooks.scripturenow.utils

import co.touchlab.kermit.Logger
import com.copperleaf.ballast.BallastLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger

public class KermitKtorLogger(private val kermitLogger: Logger) : KtorLogger {
    override fun log(message: String) {
        kermitLogger.d { message }
    }
}

public class KermitBallastLogger(private val kermitLogger: Logger) : BallastLogger {
    override fun debug(message: String) {
        kermitLogger.d { message }
    }

    override fun error(throwable: Throwable) {
        kermitLogger.e(throwable) { throwable.message ?: "" }
    }

    override fun info(message: String) {
        kermitLogger.i { message }
    }
}
