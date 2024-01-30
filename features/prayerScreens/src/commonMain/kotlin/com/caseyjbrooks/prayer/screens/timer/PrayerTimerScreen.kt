package com.caseyjbrooks.prayer.screens.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.ui.koin.LocalKoin
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

    @Composable
    internal fun Content(
        uiState: PrayerTimerContract.State,
        postInput: (PrayerTimerContract.Inputs) -> Unit,
    ) {
        Column {
            Text("Prayer Timer: PrayerId=${uiState.prayerId}")

            Button({ postInput(PrayerTimerContract.Inputs.NavigateUp) }) {
                Text("Navigate Up")
            }
            Button({ postInput(PrayerTimerContract.Inputs.GoBack) }) {
                Text("Go Back")
            }

            if (uiState.cachedPrayer.isLoading()) {
                CircularProgressIndicator()
            } else {
                val prayer = uiState.cachedPrayer.getCachedOrThrow()

                Text(prayer.text)
                Text(prayer.tags.joinToString())
            }

            Divider()

            when {
                uiState.isStopped -> {
                    Text("Timer Stopped")
                    Button(onClick = { postInput(PrayerTimerContract.Inputs.StartTimer) }) {
                        Text("Start Timer")
                    }
                    Text("${uiState.totalTime}", style = MaterialTheme.typography.headlineLarge)
                }
                !uiState.isStopped && !uiState.running -> {
                    Text("Timer Paused")
                    Button(onClick = { postInput(PrayerTimerContract.Inputs.ResumeTimer) }) {
                        Text("Resume")
                    }

                    Text("${uiState.currentTime}", style = MaterialTheme.typography.headlineLarge)
                }
                !uiState.isStopped && uiState.running -> {
                    Text("Timer Running")
                    Button(onClick = { postInput(PrayerTimerContract.Inputs.PauseTimer) }) {
                        Text("Pause")
                    }
                    Button(onClick = { postInput(PrayerTimerContract.Inputs.StopTimer) }) {
                        Text("Stop")
                    }
                    Button(onClick = { postInput(PrayerTimerContract.Inputs.ResetTimer) }) {
                        Text("Reset")
                    }

                    Text("${uiState.currentTime}", style = MaterialTheme.typography.headlineLarge)
                }
            }
        }
    }
}
