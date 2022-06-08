package com.copperleaf.ballast.router

import com.copperleaf.ballast.router.routing.Destination
import com.copperleaf.ballast.router.routing.MissingDestination
import com.copperleaf.ballast.router.routing.NavToken
import com.copperleaf.ballast.router.routing.PathSegment
import com.copperleaf.ballast.router.routing.Route
import com.copperleaf.ballast.router.routing.matchDestinationOrThrow

val RouterContract.State.currentDestination: Destination?
    get() {
        return backstack
            .lastOrNull {
                it is Destination
            }
            as? Destination?
    }

val RouterContract.State.currentDestinationOrNotFound: NavToken?
    get() {
        return backstack.lastOrNull {
            when (it) {
                is Destination -> true
                is MissingDestination -> true
                else -> false
            }
        }
    }

fun Route.asStartDestination(): Destination {
    check(this.matcher.path.all { it is PathSegment.Static }) {
        "For a Route to be used as a Start Destination, it must be fully static " +
            "(no path parameters, wildcards, or tailcards)"
    }

    return this.matchDestinationOrThrow(originalRoute)
}

fun Route.asInitialBackstack(): List<Destination> {
    return listOf(this.asStartDestination())
}
