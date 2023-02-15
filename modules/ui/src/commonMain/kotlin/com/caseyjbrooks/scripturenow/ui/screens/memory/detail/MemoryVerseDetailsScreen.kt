package com.caseyjbrooks.scripturenow.ui.screens.memory.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.BottomBarLayout
import com.caseyjbrooks.scripturenow.ui.layouts.ScrollableContent
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.caseyjbrooks.scripturenow.viewmodel.memory.detail.MemoryVerseDetailsContract
import com.copperleaf.ballast.repository.cache.getCachedOrThrow

@Composable
public fun MemoryVerseDetailsScreen(verseId: String) {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) {
        injector.getMemoryVerseDetailsViewModel(
            coroutineScope,
            verseId,
        )
    }
    val vmState by vm.observeStates().collectAsState()

    MemoryVerseDetailsScreen(vmState) { vm.trySend(it) }
}

@Composable
public fun MemoryVerseDetailsScreen(
    state: MemoryVerseDetailsContract.State,
    postInput: (MemoryVerseDetailsContract.Inputs) -> Unit,
) {
    BottomBarLayout(
        title = { Text("Verse Details") },
    ) {
        ScrollableContent {
            if (state.loading) {
                CircularProgressIndicator()
            } else {
                val verse = state.memoryVerse.getCachedOrThrow()
                Card(modifier = Modifier.fillMaxWidth().padding()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text(verse.text)
                        Text(verse.reference.referenceText)
                        Text(verse.version)
                        Text(verse.notice)
                        Text(verse.verseUrl)
                        Text("Created at: ${verse.createdAt}")
                        Text("Updated at: ${verse.updatedAt}")
                    }
                }

                Spacer(Modifier.weight(1f))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { postInput(MemoryVerseDetailsContract.Inputs.SetAsMainVerse) },
                    colors = ButtonDefaults.filledTonalButtonColors(),
                    enabled = !verse.main
                ) {
                    Text("Set As Main Verse")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { postInput(MemoryVerseDetailsContract.Inputs.EditVerse) },
                ) {
                    Text("Edit")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { postInput(MemoryVerseDetailsContract.Inputs.DeleteVerse) },
                    colors = ButtonDefaults.outlinedButtonColors(),
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
