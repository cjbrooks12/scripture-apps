package com.copperleaf.scripturenow.repositories.auth

import com.copperleaf.ballast.BallastInterceptor
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.plusAssign
import com.copperleaf.scripturenow.repositories.RepositoryConfigBuilder
import com.copperleaf.scripturenow.repositories.RepositoryCoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

typealias AuthInterceptor = BallastInterceptor<AuthRepositoryContract.Inputs, Any, AuthRepositoryContract.State>

fun authRepositoryModule() = DI.Module(name = "Repository > Auth") {
    bind<AuthRepository> {
        singleton {
            AuthRepositoryImpl(
                coroutineScope = instance(tag = RepositoryCoroutineScope),
                eventBus = instance(),
                inputHandler = AuthRepositoryInputHandler(
                    eventBus = instance(),
                    authenticationProvider = instance(),
                ),
                configBuilder = instance<BallastViewModelConfiguration.Builder>(tag = RepositoryConfigBuilder)
                    .apply {
                        this += instance<Set<AuthInterceptor>>()
                    },
            )
        }
    }
}
