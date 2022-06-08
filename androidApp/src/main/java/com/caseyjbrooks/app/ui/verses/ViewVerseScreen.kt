package com.caseyjbrooks.app.ui.verses

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.ballast.router.routing.Route
import com.copperleaf.scripturenow.ui.Destinations

class ViewVerseScreen : ComposeScreen() {
    override val screenName: String = "ViewVerseScreen"

    override val route: Route = Destinations.App.Verses.Detail

    @Composable
    override fun ScreenContent() {
        Text(screenName)
    }
}
