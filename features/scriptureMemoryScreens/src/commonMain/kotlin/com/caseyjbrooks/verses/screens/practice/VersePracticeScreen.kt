package com.caseyjbrooks.verses.screens.practice

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.ui.koin.LocalKoin
import com.caseyjbrooks.verses.models.VerseId
import com.copperleaf.ballast.repository.cache.getCachedOrElse
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading
import org.koin.core.parameter.parametersOf

public object VersePracticeScreen {
    @Composable
    public fun Content(verseId: VerseId) {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: VersePracticeViewModel = remember(coroutineScope, koin) {
            koin.get { parametersOf(coroutineScope, verseId) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Content(
        uiState: VersePracticeContract.State,
        postInput: (VersePracticeContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Verse Practice") },
                navigationIcon = {
                    IconButton(onClick = { postInput(VersePracticeContract.Inputs.NavigateUp) }) {
                        Icon(Icons.Default.ArrowBack, "Navigate Up")
                    }
                },
                actions = {
                    IconButton({ postInput(VersePracticeContract.Inputs.TogglePeek) }) {
                        if (uiState.peeking) {
                            Icon(Icons.Default.Visibility, "Turn Peek Off")
                        } else {
                            Icon(Icons.Default.VisibilityOff, "Turn Peek On")
                        }
                    }
                }
            )
            if (uiState.cachedVerse.isLoading()) {
                LoadingState(uiState, postInput)
            } else {
                val verse = uiState.cachedVerse.getCachedOrNull()
                if (verse == null) {
                    EmptyContentState(uiState, postInput)
                } else {
                    NonEmptyContentState(uiState, postInput)
                }
            }
        }
    }

    @Composable
    private fun ColumnScope.LoadingState(
        uiState: VersePracticeContract.State,
        postInput: (VersePracticeContract.Inputs) -> Unit,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    private fun ColumnScope.EmptyContentState(
        uiState: VersePracticeContract.State,
        postInput: (VersePracticeContract.Inputs) -> Unit,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("Uh-oh! That verse seems to be missing.")
        }
    }

    @Composable
    private fun ColumnScope.NonEmptyContentState(
        uiState: VersePracticeContract.State,
        postInput: (VersePracticeContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
        ) {
            val verse = uiState.cachedVerse.getCachedOrThrow()

            Box(
                Modifier
                    .fillMaxWidth()
                    .border(Dp.Hairline, MaterialTheme.colorScheme.onBackground)
                    .padding(8.dp, 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(verse.reference, style = MaterialTheme.typography.bodyLarge)
            }

            Divider()
            Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Column(Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(uiState.maskedText.getCachedOrElse { "" })
                }
            }
            Row(Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.Center) {
                var threshold by remember(uiState.threshold) { mutableStateOf(uiState.threshold) }

                Slider(
                    value = threshold,
                    onValueChange = {
                        threshold = it
                        postInput(VersePracticeContract.Inputs.UpdateThreshold(threshold))
                    },
                    onValueChangeFinished = { postInput(VersePracticeContract.Inputs.UpdateThreshold(threshold)) }
                )
            }
        }
    }
}
