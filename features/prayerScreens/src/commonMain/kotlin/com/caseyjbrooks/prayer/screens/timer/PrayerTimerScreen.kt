package com.caseyjbrooks.prayer.screens.timer

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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.ui.koin.LocalKoin
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading
import org.koin.core.parameter.parametersOf

public object PrayerTimerScreen {
    @Composable
    public fun Content(prayerId: PrayerId) {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: PrayerTimerViewModel = remember(coroutineScope, koin) {
            koin.get { parametersOf(coroutineScope, prayerId) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Content(
        uiState: PrayerTimerContract.State,
        postInput: (PrayerTimerContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Prayer Timer") },
                navigationIcon = {
                    IconButton(onClick = { postInput(PrayerTimerContract.Inputs.NavigateUp) }) {
                        Icon(Icons.Default.ArrowBack, "Navigate Up")
                    }
                },
            )
            if (uiState.cachedPrayer.isLoading()) {
                LoadingState(uiState, postInput)
            } else {
                val prayer = uiState.cachedPrayer.getCachedOrNull()
                if (prayer == null) {
                    EmptyContentState(uiState, postInput)
                } else {
                    NonEmptyContentState(uiState, postInput)
                }
            }
        }
    }

    @Composable
    private fun ColumnScope.LoadingState(
        uiState: PrayerTimerContract.State,
        postInput: (PrayerTimerContract.Inputs) -> Unit,
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
        uiState: PrayerTimerContract.State,
        postInput: (PrayerTimerContract.Inputs) -> Unit,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("Uh-oh! That prayer seems to be missing.")
        }
    }

    @Composable
    private fun ColumnScope.NonEmptyContentState(
        uiState: PrayerTimerContract.State,
        postInput: (PrayerTimerContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
        ) {
            val prayer = uiState.cachedPrayer.getCachedOrThrow()

            Box(
                Modifier
                    .fillMaxWidth()
                    .border(Dp.Hairline, MaterialTheme.colorScheme.onBackground)
                    .padding(8.dp, 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(prayer.text, style = MaterialTheme.typography.bodyLarge)
            }

            Divider()

            when {
                uiState.isStopped -> {
                    TimerStoppedContent(uiState, postInput)
                }

                !uiState.isStopped && !uiState.running -> {
                    TimerPausedContent(uiState, postInput)
                }

                !uiState.isStopped && uiState.running -> {
                    TimerRunningContent(uiState, postInput)
                }
            }
        }
    }

    @Composable
    private fun ColumnScope.TimerStoppedContent(
        uiState: PrayerTimerContract.State,
        postInput: (PrayerTimerContract.Inputs) -> Unit,
    ) {
        Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            Column(Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Timer Stopped")
                Text("${uiState.totalTime}", style = MaterialTheme.typography.headlineLarge)
            }
        }
        Row(Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { postInput(PrayerTimerContract.Inputs.StartTimer) }) {
                Text("Start Timer")
            }
        }
    }

    @Composable
    private fun ColumnScope.TimerPausedContent(
        uiState: PrayerTimerContract.State,
        postInput: (PrayerTimerContract.Inputs) -> Unit,
    ) {
        Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            Column(Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Timer Paused")
                Text("${uiState.totalTime}", style = MaterialTheme.typography.headlineLarge)
            }
        }
        Row(Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { postInput(PrayerTimerContract.Inputs.ResumeTimer) }) {
                Text("Resume")
            }
        }
    }

    @Composable
    private fun ColumnScope.TimerRunningContent(
        uiState: PrayerTimerContract.State,
        postInput: (PrayerTimerContract.Inputs) -> Unit,
    ) {
        Box(Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            Column(Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Timer Running")
                Text("${uiState.currentTime}", style = MaterialTheme.typography.headlineLarge)
            }
        }
        Row(Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.Center) {
            Button(onClick = { postInput(PrayerTimerContract.Inputs.PauseTimer) }) {
                Text("Pause")
            }
            Button(onClick = { postInput(PrayerTimerContract.Inputs.StopTimer) }) {
                Text("Stop")
            }
            Button(onClick = { postInput(PrayerTimerContract.Inputs.ResetTimer) }) {
                Text("Reset")
            }
        }
    }
}
