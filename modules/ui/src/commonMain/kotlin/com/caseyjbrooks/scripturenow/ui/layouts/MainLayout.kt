package com.caseyjbrooks.scripturenow.ui.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SignLanguage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.ui.LocalRouter
import com.caseyjbrooks.scripturenow.ui.theme.BrandIcons
import com.copperleaf.ballast.navigation.routing.*
import com.copperleaf.ballast.navigation.vm.Router

@Composable
public fun MainLayout(
    title: @Composable () -> Unit,
    content: @Composable PaddingValues.() -> Unit,
) {
    val router = LocalRouter.current
    val routerState by router.observeStates().collectAsState()
    val currentRoute = routerState.currentRouteOrNull

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = title,
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarRouteItem(
                    router,
                    currentRoute,
                    ScriptureNowRoute.Home,
                    "Home",
                    BrandIcons.Home,
                )
                NavigationBarRouteItem(
                    router,
                    currentRoute,
                    ScriptureNowRoute.MemoryVerseList,
                    "Verses",
                    BrandIcons.Bookmark,
                )
                NavigationBarRouteItem(
                    router,
                    currentRoute,
                    ScriptureNowRoute.PrayerList,
                    "Prayer",
                    BrandIcons.SignLanguage,
                )
                NavigationBarRouteItem(
                    router,
                    currentRoute,
                    ScriptureNowRoute.Settings,
                    "Settings",
                    BrandIcons.Settings,
                )
            }
        },
        content = content
    )
}

@Composable
private fun RowScope.NavigationBarRouteItem(
    router: Router<ScriptureNowRoute>,
    currentRoute: ScriptureNowRoute?,

    targetRoute: ScriptureNowRoute,
    labelText: String,
    icon: ImageVector,
) {
    NavigationBarItem(
        label = { Text(labelText) },
        icon = { Icon(icon, labelText) },
        selected = currentRoute == targetRoute,
        onClick = {
            router.trySend(
                RouterContract.Inputs.ReplaceTopDestination(
                    targetRoute.directions().build()
                )
            )
        }
    )
}

@Composable
public fun PaddingValues.ScrollableContent(
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit,
) {
    val paddingValues = this
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = verticalArrangement,
    ) {
        content()
    }
}
