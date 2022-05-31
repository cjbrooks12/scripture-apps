package com.copperleaf.scripturenow.di.kodein

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory

fun mainApplicationModule(): DI.Module = DI.Module(name = "Application >> Main") {
    bind<Logger> {
        factory {tag: String ->
            Logger(
                StaticConfig(
                    minSeverity = Severity.Verbose,
                    logWriterList = listOf(
                        platformLogWriter(),
                    )
                )
            ).withTag(tag)
        }
    }
}

expect fun platformApplicationModule(): DI.Module
