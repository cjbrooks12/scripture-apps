package com.caseyjbrooks.scripturenow.ui.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SignLanguage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.ui.LocalRouter
import com.caseyjbrooks.scripturenow.ui.theme.BrandIcons
import com.caseyjbrooks.scripturenow.ui.widgets.backHandler
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.currentRouteOrNull
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.vm.Router
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrEmptyList

@Composable
public fun BottomBarLayout(
    title: @Composable () -> Unit,
    backHandler: @Composable () -> Unit = {
        val router = LocalRouter.current
        backHandler { router.trySend(RouterContract.Inputs.GoBack()) }
    },
    content: @Composable PaddingValues.() -> Unit,
) {
    val router = LocalRouter.current
    val routerState by router.observeStates().collectAsState()
    val currentRoute = routerState.currentRouteOrNull

    backHandler()

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
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val paddingValues = this
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = verticalArrangement,
    ) {
        content()
    }
}

@Composable
public fun PaddingValues.LazyContent(
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: LazyListScope.() -> Unit,
) {
    val paddingValues = this
    LazyColumn(
        modifier = Modifier,
        contentPadding = paddingValues,
        verticalArrangement = verticalArrangement,
    ) {
        content()
    }
}

@Composable
public fun <T : Any> PaddingValues.LazyContent(
    items: Cached<List<T>>,
    onItemClick: (T) -> Unit,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    beforeItems: (LazyListScope.() -> Unit)? = null,
    afterItems: (LazyListScope.() -> Unit)? = null,
    itemContent: @Composable (T) -> Unit,
) {
    val paddingValues = this
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        contentPadding = paddingValues,
        verticalArrangement = verticalArrangement,
    ) {
        beforeItems?.invoke(this)
        items(items.getCachedOrEmptyList()) { item ->
            Card(
                onClick = { onItemClick(item) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    itemContent(item)
                }
            }
        }
        afterItems?.invoke(this)
    }
}
