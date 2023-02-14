package com.caseyjbrooks.scripturenow.ui.screens.memory.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.LazyContent
import com.caseyjbrooks.scripturenow.ui.layouts.BottomBarLayout
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.caseyjbrooks.scripturenow.viewmodel.memory.list.MemoryVerseListContract

@Composable
public fun MemoryVerseListScreen() {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) { injector.getMemoryVerseListViewModel(coroutineScope) }
    val vmState by vm.observeStates().collectAsState()

    MemoryVerseListScreen(vmState) { vm.trySend(it) }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun MemoryVerseListScreen(
    state: MemoryVerseListContract.State,
    postInput: (MemoryVerseListContract.Inputs) -> Unit,
) {
    BottomBarLayout(
        title = { Text("Verses") },
    ) {
        LazyContent(
            state.verses,
            onItemClick = { postInput(MemoryVerseListContract.Inputs.ViewVerse(it)) },
            beforeItems = {
                stickyHeader {
                    Button(
                        onClick = { postInput(MemoryVerseListContract.Inputs.CreateVerse) },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Add Verse") }
                }
            }
        ) { verse ->
            Text(verse.text)
            Text(verse.reference.referenceText)
        }
    }
}
