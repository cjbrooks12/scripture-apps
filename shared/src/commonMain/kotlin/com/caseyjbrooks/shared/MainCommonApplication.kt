package com.caseyjbrooks.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.prayer.PrayerModule
import com.caseyjbrooks.prayer.ui.list.PrayerListRoute
import com.caseyjbrooks.routing.DetailPane
import com.caseyjbrooks.routing.ListPane
import com.caseyjbrooks.routing.LocalRouter
import com.caseyjbrooks.routing.LocalSizeClass
import com.caseyjbrooks.routing.RouterViewModel
import com.caseyjbrooks.routing.ScriptureNowScreen
import com.copperleaf.ballast.navigation.routing.Backstack
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.renderCurrentDestination

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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

    val windowSizeClass = calculateWindowSizeClass()
    CompositionLocalProvider(
        LocalRouter provides router,
        LocalSizeClass provides windowSizeClass.widthSizeClass,
    ) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium,
            -> {
                PhoneLayout()
            }

            WindowWidthSizeClass.Expanded -> {
                DesktopLayout()
            }
        }
    }
}

@Composable
private fun DesktopLayout() {
    val router = LocalRouter.current
    Column {
        val routerState: Backstack<ScriptureNowScreen> by router.observeStates().collectAsState()

        Row {
            Column(Modifier.wrapContentWidth()) {
                NavigationRail {
                    NavigationRailItem(
                        selected = true,
                        onClick = {
                            router.trySend(
                                RouterContract.Inputs.GoToDestination(
                                    PrayerListRoute
                                        .directions()
                                        .build(),
                                ),
                            )
                        },
                        icon = {
                            Icon(Icons.Default.ThumbUp, "Prayers")
                        },
                        label = {
                            Text("Prayers")
                        },
                    )
                }
            }
            Column(Modifier.width(240.dp)) {
                val listPaneBackstack = routerState
                    .filterIsInstance<Destination.Match<ScriptureNowScreen>>()
                    .filter { ListPane in it.annotations }
                listPaneBackstack.renderCurrentDestination(
                    route = { appScreen: ScriptureNowScreen ->
                        appScreen.Content(this)
                    },
                    notFound = {
                        Text("Not Found")
                    },
                )

                Divider()

                LazyColumn {
                    itemsIndexed(listPaneBackstack) { index, item ->
                        Text("[$index] ${item.originalDestinationUrl}")
                    }
                }
            }
            Column(Modifier.weight(1f)) {
                val detailPaneBackstack = routerState
                    .filterIsInstance<Destination.Match<ScriptureNowScreen>>()
                    .filter { DetailPane in it.annotations }
                detailPaneBackstack.renderCurrentDestination(
                    route = { appScreen: ScriptureNowScreen ->
                        appScreen.Content(this)
                    },
                    notFound = {
                        Text("Not Found")
                    },
                )
                Divider()
                LazyColumn {
                    itemsIndexed(detailPaneBackstack) { index, item ->
                        Text("[$index] ${item.originalDestinationUrl}")
                    }
                }
            }
        }
    }
}

@Composable
private fun PhoneLayout() {
    val router = LocalRouter.current

    Scaffold(
        topBar = { },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        router.trySend(
                            RouterContract.Inputs.GoToDestination(
                                PrayerListRoute
                                    .directions()
                                    .build(),
                            ),
                        )
                    },
                    icon = {
                        Icon(Icons.Default.ThumbUp, "Prayers")
                    },
                    label = {
                        Text("Prayers")
                    },
                )
            }
        },
    ) {
        Column(Modifier.padding(it)) {
            val routerState: Backstack<ScriptureNowScreen> by router.observeStates().collectAsState()
            routerState.renderCurrentDestination(
                route = { appScreen: ScriptureNowScreen ->
                    appScreen.Content(this)
                },
                notFound = {
                    Text("Not Found")
                },
            )

            Divider()

            LazyColumn {
                itemsIndexed(routerState) { index, item ->
                    Text("[$index] ${item.originalDestinationUrl}")
                }
            }
        }
    }
}
