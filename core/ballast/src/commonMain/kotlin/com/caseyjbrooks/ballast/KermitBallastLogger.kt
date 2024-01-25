package com.caseyjbrooks.ballast

import co.touchlab.kermit.Logger
import com.copperleaf.ballast.BallastLogger

public class KermitBallastLogger(
    private val logger: Logger,
) : BallastLogger {
    override fun debug(message: String) {
        logger.d(messageString = message)
    }

    override fun error(throwable: Throwable) {
        logger.e(messageString = throwable.message ?: "", throwable = throwable)
    }

    override fun info(message: String) {
        logger.i(messageString = message)
    }
}
