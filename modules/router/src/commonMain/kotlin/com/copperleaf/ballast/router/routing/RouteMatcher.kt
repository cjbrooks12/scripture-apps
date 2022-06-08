package com.copperleaf.ballast.router.routing

data class RouteMatcher(
    val path: List<PathSegment>,
    val weight: Double,
)
