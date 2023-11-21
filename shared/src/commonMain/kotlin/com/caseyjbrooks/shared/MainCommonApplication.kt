package com.caseyjbrooks.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.di.FeatureModule
import com.caseyjbrooks.routing.LocalRouter
import com.caseyjbrooks.routing.LocalSizeClass
import com.caseyjbrooks.routing.MainNavigationBar
import com.caseyjbrooks.routing.MainNavigationRail
import com.caseyjbrooks.routing.RouterViewModel
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.caseyjbrooks.routing.currentBackstack
import com.caseyjbrooks.routing.currentDetailBackstack
import com.caseyjbrooks.routing.currentListBackstack
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
public fun MainCommonApplication(featureModule: FeatureModule = MainAppModule()) {
    val coroutineScope = rememberCoroutineScope()
    val router = remember(coroutineScope) {
        RouterViewModel(
            viewModelCoroutineScope = coroutineScope,
            initialRoute = featureModule.initialRoute!!,
            allRoutes = featureModule.routes,
        )
    }

    val windowSizeClass = calculateWindowSizeClass()
    CompositionLocalProvider(
        LocalRouter provides router,
        LocalSizeClass provides windowSizeClass.widthSizeClass,
    ) {
        when (LocalSizeClass.current) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium,
            -> {
                PhoneLayout(featureModule)
            }

            WindowWidthSizeClass.Expanded -> {
                DesktopLayout(featureModule)
            }
        }
    }
}

@Composable
private fun DesktopLayout(featureModule: FeatureModule) {
    Column {
        Row {
            Column(Modifier.wrapContentWidth()) {
                MainNavigationRail(
                    (featureModule.mainNavigationItems + featureModule.secondaryNavigationItems)
                        .sortedBy { it.order },
                )
            }
            Column(Modifier.width(240.dp)) {
                currentListBackstack().renderCurrentDestination(
                    route = { appScreen: ScriptureNowScreen ->
                        appScreen.Content(this)
                    },
                    notFound = {},
                )
            }
            Column(Modifier.weight(1f)) {
                currentDetailBackstack().renderCurrentDestination(
                    route = { appScreen: ScriptureNowScreen ->
                        appScreen.Content(this)
                    },
                    notFound = {},
                )
            }
        }
    }
}

@Composable
private fun PhoneLayout(featureModule: FeatureModule) {
    Scaffold(
        topBar = { },
        bottomBar = {
            MainNavigationBar(
                featureModule.mainNavigationItems
                    .sortedBy { it.order },
            )
        },
    ) {
        Column(Modifier.padding(it)) {
            currentBackstack().renderCurrentDestination(
                route = { appScreen: ScriptureNowScreen ->
                    appScreen.Content(this)
                },
                notFound = {},
            )
        }
    }
}
