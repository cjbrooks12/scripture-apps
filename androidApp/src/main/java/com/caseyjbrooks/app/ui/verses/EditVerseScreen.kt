package com.caseyjbrooks.app.ui.verses

import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.Route

class EditVerseScreen : ComposeScreen() {
    override val screenName: String = "EditVerseScreen"

    override fun matchesRoute(route: Route): Boolean {
        return route is Destinations.App.EditVerse
    }

    @Composable
    override fun ScreenContent() {

    }
}
