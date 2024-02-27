package com.caseyjbrooks.debug.screens.logfile

import androidx.compose.runtime.Composable
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.DetailPane
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.stringPath

public object LogFileRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/developer/logs/:logFileName")
    override val annotations: Set<RouteAnnotation> = setOf(DetailPane)

    public object Directions {

    }

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        val logFileName: String by destination.stringPath()
        LogFileScreen.Content(logFileName)
    }
}
