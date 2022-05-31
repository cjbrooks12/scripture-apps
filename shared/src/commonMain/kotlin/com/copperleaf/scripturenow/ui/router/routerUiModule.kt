package com.copperleaf.scripturenow.ui.router

import com.copperleaf.scripturenow.repositories.RepositoryConfigBuilder
import com.copperleaf.scripturenow.repositories.RepositoryCoroutineScope
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun routerVmModule(
    onBackstackEmptied: () -> Unit = { },
) = DI.Module(name = "UI > Router") {
    bind<MainRouterViewModel> {
        singleton {
            MainRouterViewModel(
                instance(tag = RepositoryCoroutineScope),
                instance(tag = RepositoryConfigBuilder),
                onBackstackEmptied
            )
        }
    }
}
