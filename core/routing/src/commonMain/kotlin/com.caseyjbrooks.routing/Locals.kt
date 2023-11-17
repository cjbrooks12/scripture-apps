package com.caseyjbrooks.routing

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.eventHandler
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

public fun RouterViewModel(
    viewModelCoroutineScope: CoroutineScope,
    initialRoute: ScriptureNowScreen,
    allRoutes: List<List<ScriptureNowScreen>>,
): Router<ScriptureNowScreen> {
    val routesSortedByWeight: List<ScriptureNowScreen> = allRoutes
        .flatten()
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
