package com.caseyjbrooks.platform.configuration

import com.copperleaf.ballast.ktor.BallastSchedules
import com.copperleaf.ballast.ktor.BallastSchedulesPluginConfiguration
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.KoinApplication

fun Application.configureBallastSchedules(
    koinApplication: KoinApplication,
    configure: BallastSchedulesPluginConfiguration.() -> Unit
) {
    install(BallastSchedules) {
        configure()
    }
}
