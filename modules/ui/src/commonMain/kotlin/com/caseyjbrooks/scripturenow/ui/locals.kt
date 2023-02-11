package com.caseyjbrooks.scripturenow.ui

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.viewmodel.ViewModelsInjector
import com.copperleaf.ballast.navigation.vm.Router

public val LocalInjector: ProvidableCompositionLocal<ViewModelsInjector> = staticCompositionLocalOf {
    error("LocalInjector not provided")
}

public val LocalRouter: ProvidableCompositionLocal<Router<ScriptureNowRoute>> = compositionLocalOf {
    error("LocalRouter not provided")
}
