package com.caseyjbrooks.platform.configuration

import com.copperleaf.ballast.ktor.BallastQueue
import com.copperleaf.ballast.ktor.BallastQueuePluginConfiguration
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.KoinApplication

fun Application.configureBallastQueues(
    koinApplication: KoinApplication,
    configure: BallastQueuePluginConfiguration.() -> Unit
) {
    install(BallastQueue) {
        configure()
    }
}
