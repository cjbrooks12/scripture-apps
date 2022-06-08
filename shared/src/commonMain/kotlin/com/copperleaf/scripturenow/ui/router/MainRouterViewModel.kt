package com.copperleaf.scripturenow.ui.router

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.router.RouterContract
import com.copperleaf.ballast.router.RouterInputHandler
import com.copperleaf.ballast.router.asInitialBackstack
import com.copperleaf.ballast.router.routing.NavGraph
import com.copperleaf.scripturenow.ui.Destinations
import kotlinx.coroutines.CoroutineScope

class MainRouterViewModel(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    onBackstackEmptied: () -> Unit,
) : BasicViewModel<
    RouterContract.Inputs,
    RouterContract.Events,
    RouterContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            filter = MainRouterFilter()
            inputStrategy = FifoInputStrategy()
        }
        .forViewModel(
            inputHandler = RouterInputHandler(),
            initialState = RouterContract.State(
                navGraph = NavGraph(
                    Destinations.App.Home,
                    Destinations.App.VerseOfTheDay,
                    Destinations.App.Verses.List,
                    Destinations.App.Verses.Detail,
                    Destinations.App.Verses.Create,
                    Destinations.App.Verses.Edit,
                ),
                backstack = Destinations.App.Home.asInitialBackstack()
            ),
            name = "MainRouter",
        ),
    eventHandler = eventHandler {
        when (it) {
            is RouterContract.Events.OnBackstackEmptied -> {
                onBackstackEmptied()
            }
            else -> {
                // ignore
            }
        }
    },
)
