package com.caseyjbrooks.scripturenow.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.MainLayout
import com.caseyjbrooks.scripturenow.ui.layouts.ScrollableContent
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeContract
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull

@Composable
public fun HomeScreen() {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) { injector.getHomeViewModel(coroutineScope) }
    val vmState by vm.observeStates().collectAsState()

    HomeScreen(vmState) { vm.trySend(it) }
}

@Composable
public fun HomeScreen(
    state: HomeContract.State,
    postInput: (HomeContract.Inputs) -> Unit,
) {
    MainLayout(
        title = { Text("Scripture Now") },
    ) {
        ScrollableContent(Arrangement.spacedBy(16.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .clickable { postInput(HomeContract.Inputs.VerseOfTheDayCardClicked) }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Verse of the Day")

                    state.verseOfTheDay.getCachedOrNull()?.let {
                        Text(it.text)
                        Text(it.reference.referenceText)
                    }
                }
            }
            Card(modifier = Modifier.fillMaxWidth().padding()) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Memory verse")

                    if (state.memoryVerse is Cached.FetchingFailed) {
                        Text("No verse set")
                    } else {
                        state.memoryVerse.getCachedOrNull()
                            ?.let {
                                Text(it.text)
                                Text(it.reference.referenceText)
                            }
                    }
                }
            }
        }
    }
}
