package com.caseyjbrooks.app.ui.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.scripturenow.ui.Route

class NotFoundScreen : ComposeScreen() {
    override val screenName: String = "NotFoundScreen"

    override fun matchesRoute(route: Route): Boolean {
        return false
    }

    @Composable
    override fun ScreenContent() {
        Text("That screen cannot be found")
    }
}
