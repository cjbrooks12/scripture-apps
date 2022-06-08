package com.copperleaf.scripturenow.ui

import com.copperleaf.ballast.navigation.routing.Route

object Destinations {
    object App {
        val Home = Route("/app/home")
        val VerseOfTheDay = Route("/app/votd")
        val Settings = Route("/app/settings")

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
    BottomBarDestination(Destinations.App.Settings),
)

val allScreens = listOf(
    Destinations.App.Home,
    Destinations.App.VerseOfTheDay,
    Destinations.App.Settings,
    Destinations.App.Verses.List,
    Destinations.App.Verses.Detail,
    Destinations.App.Verses.Create,
    Destinations.App.Verses.Edit,
)
