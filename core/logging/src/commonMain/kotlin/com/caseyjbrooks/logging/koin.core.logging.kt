package com.caseyjbrooks.logging

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.loggerConfigInit
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect fun getRealPlatformLoggingModule(): Module
public val realLoggingModule: Module = module {
    includes(getRealPlatformLoggingModule())
    factory<Logger> {
        val tag: String? = it.getOrNull()
        Logger(
            loggerConfigInit(get<LogWriter>()),
            tag ?: "Scripture Now"
        )
    }
}

internal expect fun getFakePlatformLoggingModule(): Module
public val fakeLoggingModule: Module = module {
    includes(getFakePlatformLoggingModule())
}
