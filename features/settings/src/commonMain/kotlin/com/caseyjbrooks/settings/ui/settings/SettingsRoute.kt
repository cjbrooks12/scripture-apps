package com.caseyjbrooks.settings.ui.settings

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.debug.screens.devinfo.DeveloperInfoRoute
import com.caseyjbrooks.routing.ApplicationScreen
import com.caseyjbrooks.routing.ListPane
import com.caseyjbrooks.ui.routing.LocalRouter
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouteAnnotation
import com.copperleaf.ballast.navigation.routing.RouteMatcher
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions

public object SettingsRoute : ApplicationScreen {
    override val matcher: RouteMatcher = RouteMatcher.create("/settings")
    override val annotations: Set<RouteAnnotation> = setOf(ListPane)

    public object Directions

    @Composable
    override fun Content(destination: Destination.Match<ApplicationScreen>) {
        Text("Settings")

        val router = LocalRouter.current

        Button({
            router.trySend(
                RouterContract.Inputs.GoToDestination(
                    DeveloperInfoRoute
                        .directions()
                        .build()
                )
            )
        }) {
            Text("Developer Settings")
        }
    }
}
