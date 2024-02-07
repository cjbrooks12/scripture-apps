package com.caseyjbrooks.ballast

import co.touchlab.kermit.Logger
import com.caseyjbrooks.di.KoinModule
import com.copperleaf.ballast.BallastLogger
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

public class BallastKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factory<BallastLogger> { (tag: String) ->
            KermitBallastLogger(get<Logger> { parametersOf(tag) })
        }
    }
}
