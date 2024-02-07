package com.caseyjbrooks.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.LogcatWriter
import com.caseyjbrooks.di.KoinModule
import org.koin.core.module.Module
import org.koin.dsl.module

public actual class LoggingKoinPlatformModule : KoinModule {
    override fun mainModule(): Module = module {
        factory<LogWriter> {
            LogcatWriter()
        }
    }
}
