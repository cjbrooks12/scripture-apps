package com.caseyjbrooks.scripturenow.ui.screens.votd

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.MainLayout
import com.caseyjbrooks.scripturenow.ui.layouts.ScrollableContent
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.caseyjbrooks.scripturenow.viewmodel.votd.VerseOfTheDayContract
import com.copperleaf.ballast.repository.cache.getCachedOrNull

@Composable
public fun VerseOfTheDayScreen() {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) { injector.getVerseOfTheDayViewModel(coroutineScope) }
    val vmState by vm.observeStates().collectAsState()

    VerseOfTheDayScreen(vmState) { vm.trySend(it) }
}

@Composable
public fun VerseOfTheDayScreen(
    state: VerseOfTheDayContract.State,
    postInput: (VerseOfTheDayContract.Inputs) -> Unit,
) {
    MainLayout(
        title = { Text("Verse of the Day") },
    ) {
        ScrollableContent {
            Card(modifier = Modifier.fillMaxWidth().padding()) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    state.verseOfTheDay.getCachedOrNull()?.let {
                        Text(it.text)
                        Text(it.reference.referenceText)
                    }
                }
            }
            Button(
                onClick = { postInput(VerseOfTheDayContract.Inputs.SaveAsMemoryVerse) },
                enabled = !state.saved
            ) {
                if(state.saved) {
                    Text("Verse saved")
                } else {
                    Text("Save as memory verse")
                }
            }
        }
    }
}
