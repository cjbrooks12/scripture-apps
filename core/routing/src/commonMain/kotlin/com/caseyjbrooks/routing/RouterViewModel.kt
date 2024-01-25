package com.caseyjbrooks.routing

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.RoutingTable
import com.copperleaf.ballast.navigation.routing.UnmatchedDestination
import com.copperleaf.ballast.navigation.routing.asMismatchedDestination
import com.copperleaf.ballast.navigation.routing.matchDestinationOrNull
import com.copperleaf.ballast.navigation.vm.BasicRouter
import com.copperleaf.ballast.navigation.vm.withRouter
import kotlinx.coroutines.CoroutineScope

public class RouterViewModel(
    config: BallastViewModelConfiguration<
            RouterContract.Inputs<ScriptureNowScreen>,
            RouterContract.Events<ScriptureNowScreen>,
            RouterContract.State<ScriptureNowScreen>>,
    coroutineScope: CoroutineScope,
) : BasicRouter<ScriptureNowScreen>(
    config = config,
    eventHandler = eventHandler { },
    coroutineScope = coroutineScope,
)

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
