package com.caseyjbrooks.app.ui.home

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.ballast.navigation.routing.Route

class NotFoundScreen : ComposeScreen() {
    override val screenName: String = "NotFoundScreen"

    override val route: Route? = null

    @Composable
    override fun ScreenContent() {
        Text("That screen cannot be found")
    }
}
