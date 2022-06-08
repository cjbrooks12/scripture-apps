package com.copperleaf.scripturenow.ui.router

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.routing.NavGraph
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.withRouter
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
            inputStrategy = FifoInputStrategy()
        }
        .withRouter(
            NavGraph(
                Destinations.App.Home,
                Destinations.App.VerseOfTheDay,
                Destinations.App.Verses.List,
                Destinations.App.Verses.Detail,
                Destinations.App.Verses.Create,
                Destinations.App.Verses.Edit,
            )
        )
        .build(),
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
