package com.copperleaf.scripturenow.di.kodein

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import com.copperleaf.ballast.debugger.BallastDebuggerClientConnection
import com.copperleaf.scripturenow.repositories.RepositoryCoroutineScope
import io.ktor.client.engine.HttpClientEngineFactory
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.singleton

const val MainDispatcher = "MainDispatcher"
const val BackgroundDispatcher = "BackgroundDispatcher"

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
    bind<BallastDebuggerClientConnection<*>> {
        singleton {
            BallastDebuggerClientConnection(
                instance<HttpClientEngineFactory<*>>(),
                instance(tag = RepositoryCoroutineScope),
                host = "10.0.2.2"
            ).apply { connect() }
        }
    }
}

expect fun platformApplicationModule(): DI.Module
