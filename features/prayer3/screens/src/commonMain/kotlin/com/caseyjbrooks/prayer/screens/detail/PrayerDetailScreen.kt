package com.caseyjbrooks.prayer.screens.detail

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

public object PrayerDetailScreen {
    @Composable
    public fun Content(prayerId: PrayerId) {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: PrayerDetailViewModel = remember(coroutineScope, koin) {
            koin.get(named("PrayerDetailViewModel")) { parametersOf(coroutineScope, prayerId) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @Composable
    internal fun Content(
        uiState: PrayerDetailContract.State,
        postInput: (PrayerDetailContract.Inputs) -> Unit,
    ) {
        Column {
            Text("Prayer Detail: PrayerId=${uiState.prayerId}")

            Button({ postInput(PrayerDetailContract.Inputs.NavigateUp) }) {
                Text("Navigate Up")
            }
            Button({ postInput(PrayerDetailContract.Inputs.GoBack) }) {
                Text("Go Back")
            }

            if (uiState.cachedPrayer.isLoading()) {
                CircularProgressIndicator()
            } else {
                val prayer = uiState.cachedPrayer.getCachedOrThrow()

                Text(prayer.text)
                Text(prayer.tags.joinToString())
                Button({ postInput(PrayerDetailContract.Inputs.Edit) }) {
                    Text("Edit")
                }
                Button({ postInput(PrayerDetailContract.Inputs.PrayNow) }) {
                    Text("Pray Now")
                }
            }
        }
    }
}
