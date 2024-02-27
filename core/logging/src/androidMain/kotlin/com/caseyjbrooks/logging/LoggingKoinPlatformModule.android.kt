package com.caseyjbrooks.logging

import co.touchlab.kermit.LogcatWriter
import com.caseyjbrooks.di.KoinModule
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module

public actual class LoggingKoinPlatformModule : KoinModule {
    override fun mainModule(): Module = module {
        factory<ConsoleLogWriter> {
            ConsoleLogWriter(LogcatWriter())
        }
    }

    override fun localModule(): Module = module {
        single<FileLogWriter> {
            FileLogWriter(
                AndroidFileLogWriter(
                    context = get(),
                    clock = get(),
                    timeZone = get(),
                    coroutineScope = get(),
                    ioDispatcher = Dispatchers.IO,
                )
            )
        }
    }

    override fun qaModule(): Module = module {
        single<FileLogWriter> {
            FileLogWriter(
                AndroidFileLogWriter(
                    context = get(),
                    clock = get(),
                    timeZone = get(),
                    coroutineScope = get(),
                    ioDispatcher = Dispatchers.IO,
                )
            )
        }
    }

    override fun productionModule(): Module = module {
        single<FileLogWriter> {
            FileLogWriter(
                NoOpLogWriter()
            )
        }
    }

    override fun testModule(): Module = module {
        single<FileLogWriter> {
            FileLogWriter(
                NoOpLogWriter()
            )
        }
    }
}
