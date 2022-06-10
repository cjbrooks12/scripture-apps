package com.copperleaf.scripturenow.repositories.router

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.routing.NavGraph
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.withRouter
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.allScreens
import kotlinx.coroutines.CoroutineScope

fun interface BackstackEmptiedCallback {
    fun onBackstackEmptied()
}

class MainRouterViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    onBackstackEmptied: BackstackEmptiedCallback,
) : BasicViewModel<
    RouterContract.Inputs,
    RouterContract.Events,
    RouterContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            inputStrategy = FifoInputStrategy()
        }
        .withRouter(
            navGraph = NavGraph(allScreens),
            initialRoute = Destinations.App.Settings,
        )
        .build(),
    eventHandler = eventHandler {
        when (it) {
            is RouterContract.Events.OnBackstackEmptied -> {
                onBackstackEmptied.onBackstackEmptied()
            }
            else -> {
                // ignore
            }
        }
    },
)
