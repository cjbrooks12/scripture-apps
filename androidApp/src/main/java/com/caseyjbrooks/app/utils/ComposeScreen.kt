package com.caseyjbrooks.app.utils

import androidx.compose.runtime.Composable
import com.copperleaf.ballast.navigation.routing.Route

abstract class ComposeScreen : BaseComponent {
    override val componentType: String = "ComposeScreen"
    abstract val route: Route

    @Composable
    abstract fun ScreenContent()
}
