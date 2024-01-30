package com.caseyjbrooks.logging

import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.LogWriter
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual fun getRealPlatformLoggingModule(): Module = module {
    factory<LogWriter> {
        CommonWriter()
    }
}

internal actual fun getFakePlatformLoggingModule(): Module = module {
    factory<LogWriter> {
        CommonWriter()
    }
}
