package com.caseyjbrooks.logging

import co.touchlab.kermit.ConsoleWriter
import co.touchlab.kermit.LogWriter
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformLoggingModule(): Module = module {
    factory<LogWriter> {
        ConsoleWriter()
    }
}

internal actual fun getFakePlatformLoggingModule(): Module = module {
    factory<LogWriter> {
        ConsoleWriter()
    }
}
