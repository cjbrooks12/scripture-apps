package com.caseyjbrooks.app.ui.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.scripturenow.ui.Destinations

class HomeScreen : ComposeScreen() {
    override val screenName: String = "HomeScreen"
    override val route: Route = Destinations.App.Home

    @Composable
    override fun ScreenContent() {
        Text(screenName)
    }
}
