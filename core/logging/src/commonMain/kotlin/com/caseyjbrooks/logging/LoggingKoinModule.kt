package com.caseyjbrooks.logging

import co.touchlab.kermit.Logger
import co.touchlab.kermit.loggerConfigInit
import com.caseyjbrooks.di.KoinModule
import org.koin.core.module.Module
import org.koin.dsl.module

public class LoggingKoinModule : KoinModule {

    override fun platformKoinModule(): KoinModule? = LoggingKoinPlatformModule()

    override fun mainModule(): Module = module {
        factory<Logger> {
            val tag: String? = it.getOrNull()
            Logger(
                loggerConfigInit(get<ConsoleLogWriter>(), get<FileLogWriter>()),
                tag ?: "Abide"
            )
        }
    }
}
