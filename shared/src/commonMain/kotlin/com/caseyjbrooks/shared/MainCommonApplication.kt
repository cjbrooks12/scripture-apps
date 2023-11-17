package com.caseyjbrooks.shared

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.prayer.PrayerModule
import com.caseyjbrooks.prayer.ui.list.PrayerListRoute
import com.caseyjbrooks.routing.RouterViewModel
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.navigation.routing.Backstack
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination

@Composable
public fun MainCommonApplication() {
    val coroutineScope = rememberCoroutineScope()
    val router = remember(coroutineScope) {
        RouterViewModel(
            viewModelCoroutineScope = coroutineScope,
            initialRoute = PrayerListRoute,
            allRoutes = listOf(
                PrayerModule.routes(),
            ),
        )
    }

    val routerState: Backstack<ScriptureNowScreen> by router.observeStates().collectAsState()
    routerState.renderCurrentDestination(
        route = { appScreen: ScriptureNowScreen ->
            appScreen.Content(this)
        },
        notFound = {
            Text("Not Found")
        },
    )
}
