package com.caseyjbrooks.platform

import com.caseyjbrooks.platform.configuration.configureBallastQueues
import com.caseyjbrooks.platform.configuration.configureBallastSchedules
import com.caseyjbrooks.platform.configuration.configureDependencyInjection
import com.caseyjbrooks.platform.configuration.configureMonitoring
import com.caseyjbrooks.platform.configuration.configureRedisCache
import com.caseyjbrooks.platform.configuration.configureRouting
import com.caseyjbrooks.platform.configuration.configureSecurity
import com.caseyjbrooks.platform.configuration.configureSerialization
import com.caseyjbrooks.platform.configuration.configureWebsockets
import com.caseyjbrooks.platform.di.platformKoinModule
import com.copperleaf.ballast.ktor.BallastQueuePluginConfiguration
import com.copperleaf.ballast.ktor.BallastSchedulesPluginConfiguration
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.routing.Route
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

fun Application.applicationModuleLayout(
    additionalModules: List<Module> = emptyList(),
    publicAccessRoutes: Route.() -> Unit = { },
    authenticatedRoutes: Route.() -> Unit = { },
    schedules: BallastSchedulesPluginConfiguration.() -> Unit = { },
    queues: BallastQueuePluginConfiguration.() -> Unit,
) {
    val koinApplication = KoinApplication.init().apply {
        modules(
            module {
                factory<ApplicationEnvironment> { environment }
            },
            platformKoinModule,
            *additionalModules.toTypedArray(),
        )
    }
    koinApplication.createEagerInstances()

    configureMonitoring(koinApplication)
    configureSecurity(koinApplication)
    configureSerialization(koinApplication)
    configureRedisCache(koinApplication)
    configureDependencyInjection(koinApplication)
    configureBallastSchedules(koinApplication, schedules)
    configureBallastQueues(koinApplication, queues)
    configureWebsockets(koinApplication)

    configureRouting(koinApplication, publicAccessRoutes, authenticatedRoutes)
}
