package com.copperleaf.scripturenow.ui

import com.copperleaf.ballast.router.routing.Route

object Destinations {
    object App {
        val Home = Route("/app/home")
        val VerseOfTheDay = Route("/app/votd")

        object Verses {
            val List = Route("/app/verses")
            val Detail = Route("/app/verses/{verseId}")
            val Create = Route("/app/verses/new")
            val Edit = Route("/app/verses/{verseId}/edit")
        }
    }
}

data class BottomBarDestination(
    val route: Route,
    val target: String = route.originalRoute,
)

val bottomBarDestinations = listOf(
    BottomBarDestination(Destinations.App.Home),
    BottomBarDestination(Destinations.App.VerseOfTheDay),
    BottomBarDestination(Destinations.App.Verses.List),
)
