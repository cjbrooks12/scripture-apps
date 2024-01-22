package com.caseyjbrooks.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import com.copperleaf.ballast.BallastLogger
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

public val loggingModule: Module = module {
    factory<Logger> {
        val tag: String? = it.getOrNull()
        Logger(
            loggerConfigInit(platformLogWriter()),
            tag ?: "Scripture Now"
        )
    }
    factory<BallastLogger> { (tag: String) ->
        KermitBallastLogger(get<Logger> { parametersOf(tag) })
    }
}
