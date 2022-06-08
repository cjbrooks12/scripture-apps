package com.copperleaf.ballast.router.routing

data class NavGraph internal constructor(
    val routes: List<Route>,
) {
    companion object {
        operator fun invoke(
            allRoutes: List<Route>,
        ): NavGraph {
            val routesSortedByWeight: List<Route> = allRoutes.sortedBy { it.matcher.weight }

            return NavGraph(
                routes = routesSortedByWeight
            )
        }

        operator fun invoke(
            vararg allRoutes: Route,
        ): NavGraph {
            val routesSortedByWeight: List<Route> = allRoutes.sortedBy { it.matcher.weight }

            return NavGraph(
                routes = routesSortedByWeight
            )
        }
    }
}
