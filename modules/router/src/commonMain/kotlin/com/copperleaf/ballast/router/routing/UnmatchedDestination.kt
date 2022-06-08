package com.copperleaf.ballast.router.routing

data class UnmatchedDestination(
    val originalUrl: String,

    val path: String,
    val pathSegments: List<String>,
    val query: Map<String, List<String>>,
)
