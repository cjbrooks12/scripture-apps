package com.caseyjbrooks.app.ui.home

import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.Route

class HomeScreen : ComposeScreen() {
    override val screenName: String = "HomeScreen"

    override fun matchesRoute(route: Route): Boolean {
        return route is Destinations.App.Home
    }

    @Composable
    override fun ScreenContent() {

    }
}
