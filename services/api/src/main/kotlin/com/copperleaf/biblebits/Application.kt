package com.copperleaf.biblebits

import com.copperleaf.biblebits.routing.accessRouter
import com.copperleaf.biblebits.routing.routingKoinModule
import com.copperleaf.biblebits.schedules.main.mainServerSchedule
import com.caseyjbrooks.platform.applicationModuleLayout
import com.copperleaf.biblebits.controller.controllersKoinModule
import com.copperleaf.biblebits.queues.main.mainServerQueue
import com.copperleaf.biblebits.queues.queuesKoinModule
import com.copperleaf.biblebits.repository.repositoriesKoinModule
import com.copperleaf.biblebits.routing.debugRouter
import com.copperleaf.biblebits.routing.healthCheckRouter
import com.copperleaf.biblebits.routing.metricsRouter
import com.copperleaf.biblebits.schedules.schedulesKoinModule
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import io.ktor.server.routing.route

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    applicationModuleLayout(
        additionalModules = listOf(
            schedulesKoinModule,
            queuesKoinModule,
            repositoriesKoinModule,
            controllersKoinModule,
            routingKoinModule,
        ),
        publicAccessRoutes = {
            healthCheckRouter()
            metricsRouter()
        },
        authenticatedRoutes = {
            route("/access") { accessRouter() }
            route("/debug") { debugRouter() }
        },
        schedules = {
            mainServerSchedule(this@module)
        },
        queues = {
            mainServerQueue(this@module)
        }
    )
}
