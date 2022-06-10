package com.caseyjbrooks.app.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.navigation.routing.Route
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

abstract class ComposeScreen(val route: Route) : BaseComponent {
    final override val componentType: String = "ComposeScreen"
    final override val screenName: String = route.originalRoute

    @Composable
    abstract fun screenContent(destination: Destination): Content

    sealed interface Content {
        val appBarContent: MutableState<@Composable () -> Unit>

        @Composable
        fun MainContent(contentPadding: PaddingValues)
    }

    class FullscreenContent(
        override val appBarContent: MutableState<@Composable () -> Unit>,
        val mainContent: MutableState<@Composable BoxScope.() -> Unit>,
        val contentAlignment: MutableState<Alignment>,
    ) : Content {

        @Composable
        override fun MainContent(contentPadding: PaddingValues) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding + 16.dp),
                contentAlignment = contentAlignment.value,
            ) {
                mainContent.value(this)
            }
        }
    }

    @Composable
    fun rememberFullscreenContent(
        appBarContent: @Composable () -> Unit = { },
        mainContent: @Composable BoxScope.() -> Unit = { },
        contentAlignment: Alignment = Alignment.Center,
    ): Content {
        return remember {
            FullscreenContent(
                appBarContent = mutableStateOf(appBarContent),
                mainContent = mutableStateOf(mainContent),
                contentAlignment = mutableStateOf(contentAlignment),
            )
        }.apply {
            this.appBarContent.value = appBarContent
            this.mainContent.value = mainContent
            this.contentAlignment.value = contentAlignment
        }
    }

    class ScrollableContent(
        override val appBarContent: MutableState<@Composable () -> Unit>,
        val mainContent: MutableState<@Composable ColumnScope.() -> Unit>,
    ) : Content {

        @Composable
        override fun MainContent(contentPadding: PaddingValues) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(contentPadding + 16.dp)
            ) {
                mainContent.value(this)
            }
        }
    }

    @Composable
    fun rememberScrollableContent(
        appBarContent: @Composable () -> Unit = { },
        mainContent: @Composable ColumnScope.() -> Unit = { },
    ): Content {
        return remember {
            ScrollableContent(
                appBarContent = mutableStateOf(appBarContent),
                mainContent = mutableStateOf(mainContent),
            )
        }.apply {
            this.appBarContent.value = appBarContent
            this.mainContent.value = mainContent
        }
    }

    class SwipeRefreshContent(
        override val appBarContent: MutableState<@Composable () -> Unit>,
        val mainContent: MutableState<@Composable ColumnScope.() -> Unit>,
        val isLoading: MutableState<Boolean>,
        val onRefresh: MutableState<() -> Unit>,
    ) : Content {
        @Composable
        override fun MainContent(contentPadding: PaddingValues) {
            SwipeRefresh(
                state = rememberSwipeRefreshState(isLoading.value),
                onRefresh = { onRefresh.value() },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(contentPadding + 16.dp)
                ) {
                    mainContent.value(this)
                }
            }
        }
    }

    @Composable
    fun rememberSwipeRefreshContent(
        appBarContent: @Composable () -> Unit = { },
        mainContent: @Composable ColumnScope.() -> Unit = { },
        isLoading: Boolean = false,
        onRefresh: () -> Unit = { },
    ): Content {
        return remember {
            SwipeRefreshContent(
                appBarContent = mutableStateOf(appBarContent),
                mainContent = mutableStateOf(mainContent),
                isLoading = mutableStateOf(isLoading),
                onRefresh = mutableStateOf(onRefresh),
            )
        }.apply {
            this.appBarContent.value = appBarContent
            this.mainContent.value = mainContent
            this.isLoading.value = isLoading
            this.onRefresh.value = onRefresh
        }
    }

    class SwipeRefreshLazyListContent(
        override val appBarContent: MutableState<@Composable () -> Unit>,
        val mainContent: MutableState<LazyListScope.() -> Unit>,
        val isLoading: MutableState<Boolean>,
        val onRefresh: MutableState<() -> Unit>,
    ) : Content {
        @Composable
        override fun MainContent(contentPadding: PaddingValues) {
            SwipeRefresh(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                state = rememberSwipeRefreshState(isLoading.value),
                onRefresh = { onRefresh.value() },
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    mainContent.value(this)
                }
            }
        }
    }

    @Composable
    fun rememberSwipeRefreshLazyListContent(
        appBarContent: @Composable () -> Unit = { },
        mainContent: LazyListScope.() -> Unit = { },
        isLoading: Boolean = false,
        onRefresh: () -> Unit = { },
    ): Content {
        return remember {
            SwipeRefreshLazyListContent(
                appBarContent = mutableStateOf(appBarContent),
                mainContent = mutableStateOf(mainContent),
                isLoading = mutableStateOf(isLoading),
                onRefresh = mutableStateOf(onRefresh),
            )
        }.apply {
            this.appBarContent.value = appBarContent
            this.mainContent.value = mainContent
            this.isLoading.value = isLoading
            this.onRefresh.value = onRefresh
        }
    }
}
