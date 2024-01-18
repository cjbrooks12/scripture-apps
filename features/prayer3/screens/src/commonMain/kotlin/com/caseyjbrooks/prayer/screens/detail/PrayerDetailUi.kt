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
import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCaseImpl
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.repository.saved.InMemorySavedPrayersRepository
import com.caseyjbrooks.routing.LocalRouter
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading

public object PrayerDetailUi {
    @Composable
    public fun Content(prayerId: PrayerId) {
        val coroutineScope = rememberCoroutineScope()
        val router = LocalRouter.current
        val viewModel = remember(coroutineScope) {
            PrayerDetailViewModel(
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
        uiState: com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.State,
        postInput: (com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Inputs) -> Unit,
    ) {
        Column {
            Text("Prayer Detail: PrayerId=${uiState.prayerId}")

            Button({ postInput(com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Inputs.NavigateUp) }) {
                Text("Navigate Up")
            }
            Button({ postInput(com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Inputs.GoBack) }) {
                Text("Go Back")
            }

            if (uiState.cachedPrayer.isLoading()) {
                CircularProgressIndicator()
            } else {
                val prayer = uiState.cachedPrayer.getCachedOrThrow()

                Text(prayer.text)
                Text(prayer.tags.joinToString())
                Button({ postInput(com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Inputs.Edit) }) {
                    Text("Edit")
                }
                Button({ postInput(com.caseyjbrooks.prayer.screens.detail.PrayerDetailContract.Inputs.PrayNow) }) {
                    Text("Pray Now")
                }
            }
        }
    }
}
