package com.caseyjbrooks.prayer.screens.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.routing.LocalKoin
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

public object PrayerTimerScreen {
    @Composable
    public fun Content(prayerId: PrayerId) {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: PrayerTimerViewModel = remember(coroutineScope, koin) {
            koin.get(named("PrayerTimerViewModel")) { parametersOf(coroutineScope, prayerId) }
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
        }
    }
}
