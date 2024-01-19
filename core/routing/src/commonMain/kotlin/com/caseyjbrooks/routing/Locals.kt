package com.caseyjbrooks.routing

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.navigation.routing.Backstack
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RoutingTable
import com.copperleaf.ballast.navigation.routing.UnmatchedDestination
import com.copperleaf.ballast.navigation.routing.asMismatchedDestination
import com.copperleaf.ballast.navigation.routing.matchDestinationOrNull
import com.copperleaf.ballast.navigation.vm.withRouter
import kotlinx.coroutines.CoroutineScope
import org.koin.core.Koin

public val LocalRouter: ProvidableCompositionLocal<RouterViewModel> =
    staticCompositionLocalOf<RouterViewModel> {
        error("LocalRouter not provided")
    }

public val LocalSizeClass: ProvidableCompositionLocal<WindowWidthSizeClass> =
    staticCompositionLocalOf<WindowWidthSizeClass> {
        error("LocalSizeClass not provided")
    }

public val LocalKoin: ProvidableCompositionLocal<Koin> =
    compositionLocalOf<Koin> {
        error("LocalKoin not provided")
    }

public fun RouterViewModel(
    viewModelCoroutineScope: CoroutineScope,
    initialRoute: ScriptureNowScreen,
    allRoutes: List<ScriptureNowScreen>,
): RouterViewModel {
    val routesSortedByWeight: List<ScriptureNowScreen> = allRoutes
        .sortedByDescending { it.matcher.weight }

    return RouterViewModel(
        config = BallastViewModelConfiguration.Builder()
            .withRouter(ListRoutingTable(routesSortedByWeight), initialRoute)
            .build(),
        coroutineScope = viewModelCoroutineScope,
    )
}

public data class ListRoutingTable<T>(
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
