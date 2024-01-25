package com.caseyjbrooks.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.loggerConfigInit
import co.touchlab.kermit.platformLogWriter
import org.koin.core.module.Module
import org.koin.dsl.module

public val realLoggingModule: Module = module {
    factory<Logger> {
        val tag: String? = it.getOrNull()
        Logger(
            loggerConfigInit(platformLogWriter()),
            tag ?: "Scripture Now"
        )
    }
}

public val fakeLoggingModule: Module = module {
}
