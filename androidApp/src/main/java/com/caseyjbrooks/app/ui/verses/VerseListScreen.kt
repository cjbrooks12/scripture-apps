package com.caseyjbrooks.app.ui.verses

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.scripturenow.ui.Destinations

class VerseListScreen : ComposeScreen() {
    override val screenName: String = "VerseListScreen"

    override val route: Route = Destinations.App.Verses.List

    @Composable
    override fun ScreenContent() {
        Text(screenName)
    }
}
