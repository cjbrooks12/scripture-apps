package com.caseyjbrooks.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.LogcatWriter
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformLoggingModule(): Module = module {
    factory<LogWriter> {
        LogcatWriter()
    }
}

internal actual fun getFakePlatformLoggingModule(): Module = module {
    factory<LogWriter> {
        LogcatWriter()
    }
}
