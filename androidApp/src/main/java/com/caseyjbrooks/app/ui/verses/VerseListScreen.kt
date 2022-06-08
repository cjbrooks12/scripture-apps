package com.caseyjbrooks.app.ui.verses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.app.utils.ComposeScreen
import com.caseyjbrooks.app.utils.theme.LocalInjector
import com.copperleaf.ballast.navigation.routing.Route
import com.copperleaf.ballast.repository.cache.getCachedOrEmptyList
import com.copperleaf.ballast.repository.cache.isLoading
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.verses.list.MemoryVerseListContract
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class VerseListScreen : ComposeScreen() {
    override val screenName: String = "VerseListScreen"

    override val route: Route = Destinations.App.Verses.List

    @Composable
    override fun ScreenContent() {
        Text(screenName)
        val coroutineScope = rememberCoroutineScope()
        val injector = LocalInjector.current
        val vm = remember(coroutineScope, injector) { injector.verseListViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
//                    navigationIcon = {
//                        IconButton(onClick = { vm.trySend(MemoryVerseListContract.Inputs.CreateVerse) }) {
//                            Icon(Icons.Default.ArrowBack, "Go Back")
//                        }
//                    },
                    title = { Text("Memory Verses") },
                    actions = {
                        IconButton(onClick = { vm.trySend(MemoryVerseListContract.Inputs.CreateVerse) }) {
                            Icon(Icons.Default.Add, "Create verse")
                        }
                    }
                )
            },
            content = { contentPadding ->
                SwipeRefresh(
                    state = rememberSwipeRefreshState(vmState.verses.isLoading()),
                    onRefresh = { vm.trySend(MemoryVerseListContract.Inputs.Initialize(true)) },
                ) {
                    val verses = remember(vmState) { vmState.verses.getCachedOrEmptyList() }

                    if (verses.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = contentPadding.calculateTopPadding(),
                                    bottom = contentPadding.calculateBottomPadding(),
                                    start = 16.dp,
                                    end = 16.dp,
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("You have no saved verses. Click the + above to get started", textAlign = TextAlign.Center)
                        }
                    } else {
                        LazyColumn(contentPadding = contentPadding) {
                            items(vmState.verses.getCachedOrEmptyList(), key = { it.uuid }) { verse ->
                                ListItem(
                                    modifier = Modifier.clickable { },
                                    text = { Text(verse.reference, overflow = TextOverflow.Ellipsis, maxLines = 1) },
                                    secondaryText = {
                                        Text(
                                            verse.text,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 2
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
            },
        )
    }
}
