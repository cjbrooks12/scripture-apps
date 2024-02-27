package com.caseyjbrooks.debug.screens.loglist

import androidx.compose.runtime.Composable
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.DetailPane
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

public object LogListRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/developer/logs")
    override val annotations: Set<RouteAnnotation> = setOf(DetailPane)

    public object Directions {

    }

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        LogListScreen.Content()
    }
}
