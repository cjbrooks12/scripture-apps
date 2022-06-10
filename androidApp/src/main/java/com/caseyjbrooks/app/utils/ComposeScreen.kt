package com.caseyjbrooks.app.utils

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.Route

abstract class ComposeScreen(val route: Route) : BaseComponent {
    final override val componentType: String = "ComposeScreen"
    final override val screenName: String = route.originalRoute

    @Composable
    abstract fun screenContent(destination: Destination): Content

    class Content(
        val appBarContent: MutableState<@Composable () -> Unit>,
        val mainContent: MutableState<@Composable ColumnScope.() -> Unit>,
        val swipeRefreshEnabled: MutableState<Boolean>,
        val isLoading: MutableState<Boolean>,
        val onRefresh: MutableState<() -> Unit>,
    )

    @Composable
    fun rememberHomescreenContent(
        appBarContent: @Composable () -> Unit = { },
        mainContent: @Composable ColumnScope.() -> Unit = { },
        swipeRefreshEnabled: Boolean = false,
        isLoading: Boolean = false,
        onRefresh: () -> Unit = { },
    ): Content {
        return remember {
            Content(
                appBarContent = mutableStateOf(appBarContent),
                mainContent = mutableStateOf(mainContent),
                swipeRefreshEnabled = mutableStateOf(swipeRefreshEnabled),
                isLoading = mutableStateOf(isLoading),
                onRefresh = mutableStateOf(onRefresh),
            )
        }.apply {
            this.appBarContent.value = appBarContent
            this.mainContent.value = mainContent
            this.swipeRefreshEnabled.value = swipeRefreshEnabled
            this.isLoading.value = isLoading
            this.onRefresh.value = onRefresh
        }
    }
}
