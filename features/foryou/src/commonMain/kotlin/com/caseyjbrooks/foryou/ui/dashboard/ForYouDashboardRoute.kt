package com.caseyjbrooks.foryou.ui.dashboard

import androidx.compose.runtime.Composable
import com.caseyjbrooks.routing.ListPane
import com.caseyjbrooks.routing.ApplicationScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

public object ForYouDashboardRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/home")
    override val annotations: Set<RouteAnnotation> = setOf(ListPane)

    public object Directions

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        ForYouDashboardScreen.Content()
    }
}
