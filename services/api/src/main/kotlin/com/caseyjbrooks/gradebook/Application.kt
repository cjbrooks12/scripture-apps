package com.caseyjbrooks.gradebook

import com.caseyjbrooks.gradebook.controller.controllersKoinModule

import com.caseyjbrooks.gradebook.queues.main.mainServerQueue
import com.caseyjbrooks.gradebook.queues.queuesKoinModule
import com.caseyjbrooks.gradebook.repository.repositoriesKoinModule
import com.caseyjbrooks.gradebook.routing.accessRouter
import com.caseyjbrooks.gradebook.routing.debugRouter
import com.caseyjbrooks.gradebook.routing.healthCheckRouter
import com.caseyjbrooks.gradebook.routing.metricsRouter
import com.caseyjbrooks.gradebook.routing.routingKoinModule
import com.caseyjbrooks.gradebook.schedules.main.mainServerSchedule
import com.caseyjbrooks.gradebook.schedules.schedulesKoinModule
import com.caseyjbrooks.platform.applicationModuleLayout
import io.ktor.server.application.Application
import io.ktor.server.routing.route

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
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
