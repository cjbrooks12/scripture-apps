package com.caseyjbrooks.prayer.ui.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCaseImpl
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.repository.saved.InMemorySavedPrayersRepository
import com.caseyjbrooks.routing.LocalRouter
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading

public object PrayerTimerUi {
    @Composable
    public fun Content(prayerId: PrayerId) {
        val coroutineScope = rememberCoroutineScope()
        val router = LocalRouter.current
        val viewModel = remember(coroutineScope) {
            PrayerTimerViewModel(
                coroutineScope,
                GetPrayerByIdUseCaseImpl(
                    InMemorySavedPrayersRepository.INSTANCE,
                ),
                router,
                prayerId,
            )
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
