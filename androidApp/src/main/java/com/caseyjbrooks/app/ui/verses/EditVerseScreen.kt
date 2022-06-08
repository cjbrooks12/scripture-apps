package com.caseyjbrooks.app.ui.verses

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.scripturenow.ui.Destinations

class EditVerseScreen : ComposeScreen() {
    override val screenName: String = "EditVerseScreen"

    override val route: Route = Destinations.App.Verses.Edit

    @Composable
    override fun ScreenContent() {
        Text(screenName)
    }
}
