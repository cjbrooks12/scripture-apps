package com.caseyjbrooks.app.ui.votd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.app.utils.ComposeScreen
import com.caseyjbrooks.app.utils.theme.LocalInjector
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.votd.VotdContract
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class VotdScreen : ComposeScreen(Destinations.App.VerseOfTheDay) {

    @Composable
    override fun ScreenContent(destination: Destination) {
        val coroutineScope = rememberCoroutineScope()
        val injector = LocalInjector.current
        val vm = remember(coroutineScope, injector) { injector.votdViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
//                    navigationIcon = {
//                        IconButton(onClick = { vm.trySend(MemoryVerseDetailsContract.Inputs.GoBack) }) {
//                            Icon(Icons.Default.ArrowBack, "Go Back")
//                        }
//                    },
                    title = { Text("Verse of the Day") },
//                    actions = {
//                        IconButton(onClick = { vm.trySend(MemoryVerseDetailsContract.Inputs.EditVerse) }) {
//                            Icon(Icons.Default.Edit, "Edit verse")
//                        }
//                    }
                )
            },
            content = { contentPadding ->
                SwipeRefresh(
                    state = rememberSwipeRefreshState(vmState.verseOfTheDay.isLoading()),
                    onRefresh = { vm.trySend(VotdContract.Inputs.Initialize(true)) },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(
                                top = contentPadding.calculateTopPadding() + 16.dp,
                                bottom = contentPadding.calculateBottomPadding() + 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                            )
                    ) {
                        Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                vmState.verseOfTheDay.getCachedOrNull()?.let { votd ->
                                    Text(votd.text)
                                    Text(votd.reference, fontStyle = FontStyle.Italic)
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
