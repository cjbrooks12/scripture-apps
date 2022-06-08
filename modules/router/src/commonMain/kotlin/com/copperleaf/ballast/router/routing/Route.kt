package com.copperleaf.ballast.router.routing

data class Route internal constructor(
    val originalRoute: String,
    val matcher: RouteMatcher,
) {
    companion object {
        /**
         * Create a Route and compute its weight automatically
         */
        operator fun invoke(
            originalRoute: String,
        ): Route {
            return Route(
                originalRoute = originalRoute,
                matcher = originalRoute.createMatcher()
            )
        }

        /**
         * Create a Route with a hardcoded weight
         */
        operator fun invoke(
            originalRoute: String,
            weight: Double,
        ): Route {
            return Route(
                originalRoute = originalRoute,
                matcher = originalRoute.createMatcher { weight }
            )
        }
    }
}
