package com.caseyjbrooks.scripturememory.ui.list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.routing.ListPane
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher

public object SavedVersesListRoute : ScriptureNowScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/verses")
    override val annotations: Set<RouteAnnotation> = setOf(ListPane)

    public object Directions

    @Composable
    override fun Content(destination: Destination.Match<ScriptureNowScreen>) {
        Text("Saved Verses")
    }
}
