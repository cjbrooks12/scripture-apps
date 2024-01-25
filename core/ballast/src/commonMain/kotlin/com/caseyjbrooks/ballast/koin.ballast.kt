package com.caseyjbrooks.ballast

import co.touchlab.kermit.Logger
import com.copperleaf.ballast.BallastLogger
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

internal expect fun getRealPlatformBallastModule(): Module
public val realBallastModule: Module = module {
    includes(getRealPlatformBallastModule())
    factory<BallastLogger> { (tag: String) ->
        KermitBallastLogger(get<Logger> { parametersOf(tag) })
    }
}


internal expect fun getFakePlatformBallastModule(): Module
public val fakeBallastModule: Module = module {
    includes(getFakePlatformBallastModule())
}
