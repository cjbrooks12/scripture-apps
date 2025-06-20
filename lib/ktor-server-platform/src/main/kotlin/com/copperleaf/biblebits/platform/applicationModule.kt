package com.copperleaf.biblebits.platform

import com.copperleaf.ballast.ktor.BallastQueuePluginConfiguration
import com.copperleaf.ballast.ktor.BallastSchedulesPluginConfiguration
import com.copperleaf.biblebits.platform.configuration.configureBallastQueues
import com.copperleaf.biblebits.platform.configuration.configureBallastSchedules
import com.copperleaf.biblebits.platform.configuration.configureDependencyInjection
import com.copperleaf.biblebits.platform.configuration.configureMonitoring
import com.copperleaf.biblebits.platform.configuration.configureRedisCache
import com.copperleaf.biblebits.platform.configuration.configureRouting
import com.copperleaf.biblebits.platform.configuration.configureSecurity
import com.copperleaf.biblebits.platform.configuration.configureSerialization
import com.copperleaf.biblebits.platform.configuration.configureWebsockets
import com.copperleaf.biblebits.platform.di.platformKoinModule
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.routing.Route
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Configures the default URL path layout and other standard components of the Ktor server.
 *
 * @param [additionalModules] A list of Koin modules which provide definitions injected throughout the server.
 *
 * @param [publicRoutes] Public-access routes available at `/api/v1/public`. These routes do not require authentication,
 *   and do not consider the logged-in user even if an auth token is provided.
 * @param [protectedRoutes] Routes available at `/api/v1/protected` which are available by default to any logged-in
 *   user. These routes define the standard functionality of the end-user facing application.
 * @param [secureRoutes] Routes available at `/api/v1/secure` which are available by default to any logged-in user who
 *   grants special permission for actions requiring a higher level of security, such as changing profile settings.
 * @param [adminRoutes] Routes available at `/api/v1/admin` which are available by default to any logged-in user who
 *   grants special permission for actions involving administration of the backend system. Though any user may
 *   successfully request the scope to access these routes and no longer get a 401 response, admin routes are still
 *   protected by fine-grained authorization permissions and will return a 403 for all users except system admins.
 *
 * @param [schedules] Tasks to be run on a regular schedule, that do not need to be executed in the system-wide queue.
 *   This is typically reserved for things that clean up the local memory of the machine, such as purging old entries
 *   from an in-memory cache. However, schedules may also delegate tasks to one of the registered [queues] to make the
 *   task persistent throughout the entire application cluster.
 * @param [queues] The registered queues which process persistent tasks.
 */
fun Application.applicationModuleLayout(
    additionalModules: List<Module> = emptyList(),

    publicRoutes: Route.() -> Unit = { },
    protectedRoutes: Route.() -> Unit = { },
    secureRoutes: Route.() -> Unit = { },
    adminRoutes: Route.() -> Unit = { },

    schedules: BallastSchedulesPluginConfiguration.() -> Unit = { },
    queues: BallastQueuePluginConfiguration.() -> Unit = { },
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

    configureRouting(
        apiVersion = 1,
        koinApplication = koinApplication,
        publicRoutes = publicRoutes,
        protectedRoutes = protectedRoutes,
        secureRoutes = secureRoutes,
        adminRoutes = adminRoutes,
    )
}
