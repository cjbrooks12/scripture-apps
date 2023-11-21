package com.caseyjbrooks.routing

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.routing.Backstack
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RoutingTable
import com.copperleaf.ballast.navigation.routing.UnmatchedDestination
import com.copperleaf.ballast.navigation.routing.asMismatchedDestination
import com.copperleaf.ballast.navigation.routing.matchDestinationOrNull
import com.copperleaf.ballast.navigation.vm.BasicRouter
import com.copperleaf.ballast.navigation.vm.Router
import com.copperleaf.ballast.navigation.vm.withRouter
import kotlinx.coroutines.CoroutineScope

public val LocalRouter: ProvidableCompositionLocal<Router<ScriptureNowScreen>> =
    staticCompositionLocalOf<Router<ScriptureNowScreen>> {
        error("LocalRouter not provided")
    }

public val LocalSizeClass: ProvidableCompositionLocal<WindowWidthSizeClass> =
    staticCompositionLocalOf<WindowWidthSizeClass> {
        error("LocalSizeClass not provided")
    }

public fun RouterViewModel(
    viewModelCoroutineScope: CoroutineScope,
    initialRoute: ScriptureNowScreen,
    allRoutes: List<ScriptureNowScreen>,
): Router<ScriptureNowScreen> {
    val routesSortedByWeight: List<ScriptureNowScreen> = allRoutes
        .sortedByDescending { it.matcher.weight }

    return BasicRouter(
        config = BallastViewModelConfiguration.Builder()
            .withRouter(ListRoutingTable(routesSortedByWeight), initialRoute)
            .build(),
        eventHandler = eventHandler { },
        coroutineScope = viewModelCoroutineScope,
    )
}

internal data class ListRoutingTable<T>(
    private val routes: List<T>,
) : RoutingTable<T> where T : Route {
    override fun findMatch(
        unmatchedDestination: UnmatchedDestination,
    ): Destination<T> {
        return routes
            .firstNotNullOfOrNull { it.matcher.matchDestinationOrNull(it, unmatchedDestination) }
            ?: unmatchedDestination.asMismatchedDestination()
    }
}

@Composable
public fun currentBackstack(): Backstack<ScriptureNowScreen> {
    val router = LocalRouter.current
    val routerState: Backstack<ScriptureNowScreen> by router.observeStates().collectAsState()
    return routerState
}

@Composable
public fun currentListBackstack(): Backstack<ScriptureNowScreen> {
    val router = LocalRouter.current
    val routerState: Backstack<ScriptureNowScreen> by router.observeStates().collectAsState()
    return routerState
        .filterIsInstance<Destination.Match<ScriptureNowScreen>>()
        .filter { ListPane in it.annotations }
}

@Composable
public fun currentDetailBackstack(): Backstack<ScriptureNowScreen> {
    val router = LocalRouter.current
    val routerState: Backstack<ScriptureNowScreen> by router.observeStates().collectAsState()
    return routerState
        .filterIsInstance<Destination.Match<ScriptureNowScreen>>()
        .filter { DetailPane in it.annotations }
}
