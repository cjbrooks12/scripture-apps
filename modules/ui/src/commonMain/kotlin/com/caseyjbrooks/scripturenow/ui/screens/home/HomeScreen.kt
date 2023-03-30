package com.caseyjbrooks.scripturenow.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.BottomBarLayout
import com.caseyjbrooks.scripturenow.ui.layouts.ScrollableContent
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.caseyjbrooks.scripturenow.viewmodel.home.HomeContract
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import kotlinx.coroutines.Dispatchers

@Composable
public fun HomeScreen() {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) { injector.getHomeViewModel(coroutineScope) }
    val vmState by vm.observeStates().collectAsState(Dispatchers.Main.immediate)

    HomeScreen(vmState) { vm.trySend(it) }
}

@Composable
public fun HomeScreen(
    state: HomeContract.State,
    postInput: (HomeContract.Inputs) -> Unit,
) {
    BottomBarLayout(
        title = { Text("Scripture Now") },
    ) {
        ScrollableContent {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { postInput(HomeContract.Inputs.MemoryVerseCardClicked) }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Main Memory verse")

                    if (state.mainMemoryVerse is Cached.FetchingFailed) {
                        Text("No verse set")
                    } else {
                        state.mainMemoryVerse.getCachedOrNull()
                            ?.let {
                                Text(it.text)
                                Text(it.reference.referenceText)
                            }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { postInput(HomeContract.Inputs.VerseOfTheDayCardClicked) }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Verse of the Day")

                    state.verseOfTheDay.getCachedOrNull()?.let {
                        Text(it.text)
                        Text(it.reference.referenceText)
                        Text(it.notice)
                    }
                }
            }
        }
    }
}
