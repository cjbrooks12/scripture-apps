package com.caseyjbrooks.app.ui.verses

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.scripturenow.ui.Destinations

class NewVerseScreen : ComposeScreen() {
    override val screenName: String = "NewVerseScreen"

    override val route: Route = Destinations.App.Verses.Create

    @Composable
    override fun ScreenContent() {
        Text(screenName)
    }
}
