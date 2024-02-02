package com.caseyjbrooks.settings.ui.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.routing.ListPane
import com.caseyjbrooks.routing.ApplicationScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

public object SettingsRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/settings")
    override val annotations: Set<RouteAnnotation> = setOf(ListPane)

    public object Directions

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        Text("Settings")
    }
}
