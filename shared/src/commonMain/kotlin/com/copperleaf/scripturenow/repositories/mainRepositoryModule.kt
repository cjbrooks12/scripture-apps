package com.copperleaf.scripturenow.repositories

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.LoggingInterceptor
import com.copperleaf.ballast.debugger.BallastDebuggerClientConnection
import com.copperleaf.ballast.debugger.BallastDebuggerInterceptor
import com.copperleaf.ballast.dispatchers
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.bus.EventBusImpl
import com.copperleaf.scripturenow.di.kodein.BackgroundDispatcher
import com.copperleaf.scripturenow.utils.KermitBallastLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton

const val RepositoryCoroutineScope = "RepositoryCoroutineScope"
const val RepositoryConfigBuilder = "RepositoryConfigBuilder"

fun mainRepositoryModule() = DI.Module(name = "Repository >> Main") {
    bind<CoroutineScope>(tag = RepositoryCoroutineScope) {
        singleton { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
    }
    bind<EventBus>() {
        singleton { EventBusImpl() }
    }
    bind<BallastViewModelConfiguration.Builder>(tag = RepositoryConfigBuilder) {
        provider {
            BallastViewModelConfiguration.Builder()
                .dispatchers(
                    inputsDispatcher = instance(tag = BackgroundDispatcher),
                    eventsDispatcher = instance(tag = BackgroundDispatcher),
                    sideJobsDispatcher = instance(tag = BackgroundDispatcher),
                    interceptorDispatcher = instance(tag = BackgroundDispatcher),
                )
                .apply {
                    this += LoggingInterceptor()
                    logger = { tag -> KermitBallastLogger(instance(arg = tag)) }

                    this += BallastDebuggerInterceptor(instance<BallastDebuggerClientConnection<*>>())
                }
        }
    }
}
