package com.caseyjbrooks.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.OSLogWriter
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformLoggingModule(): Module = module {
    factory<LogWriter> {
        OSLogWriter()
    }
}

internal actual fun getFakePlatformLoggingModule(): Module = module {
    factory<LogWriter> {
        OSLogWriter()
    }
}
