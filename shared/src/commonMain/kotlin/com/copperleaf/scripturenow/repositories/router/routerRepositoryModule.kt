package com.copperleaf.scripturenow.repositories.router

import com.copperleaf.ballast.BallastInterceptor
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.plusAssign
import com.copperleaf.scripturenow.repositories.RepositoryConfigBuilder
import com.copperleaf.scripturenow.repositories.RepositoryCoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

typealias RouterInterceptor = BallastInterceptor<RouterContract.Inputs, RouterContract.Events, RouterContract.State>

fun routerRepositoryModule(
    onBackstackEmptied: () -> Unit = { },
) = DI.Module(name = "Repository > Router") {
    bind<MainRouterViewModel> {
        singleton {
            MainRouterViewModel(
                instance(tag = RepositoryCoroutineScope),
                instance<BallastViewModelConfiguration.Builder>(tag = RepositoryConfigBuilder)
                    .apply {
                        this += instance<Set<RouterInterceptor>>()
                    },
                onBackstackEmptied
            )
        }
    }
}
