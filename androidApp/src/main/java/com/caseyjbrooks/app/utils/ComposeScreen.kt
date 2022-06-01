package com.caseyjbrooks.app.utils

import androidx.compose.runtime.Composable
import com.copperleaf.scripturenow.ui.Route

abstract class ComposeScreen : BaseComponent {
    override val componentType: String = "ComposeScreen"

    abstract fun matchesRoute(route: Route): Boolean

    @Composable
    abstract fun ScreenContent()
}
