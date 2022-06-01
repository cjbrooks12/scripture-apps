package com.caseyjbrooks.app.ui.verses

import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.Route

class ViewVerseScreen : ComposeScreen() {
    override val screenName: String = "HomeScreen"

    override fun matchesRoute(route: Route): Boolean {
        return route is Destinations.App.ViewVerse
    }

    @Composable
    override fun ScreenContent() {

    }
}
