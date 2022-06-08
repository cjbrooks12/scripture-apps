package com.caseyjbrooks.app.utils

import androidx.compose.runtime.Composable
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.Route

abstract class ComposeScreen(val route: Route) : BaseComponent {
    final override val componentType: String = "ComposeScreen"
    final override val screenName: String = route.originalRoute

    @Composable
    abstract fun ScreenContent(destination: Destination)
}
