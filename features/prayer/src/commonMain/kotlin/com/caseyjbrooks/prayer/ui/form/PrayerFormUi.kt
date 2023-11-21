package com.caseyjbrooks.prayer.ui.form

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

public object PrayerFormUi {
    @Composable
    public fun Content(prayerId: PrayerId?) {
        val coroutineScope = rememberCoroutineScope()
        val router = LocalRouter.current
        val viewModel = remember(coroutineScope) {
            PrayerFormViewModel(
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
        uiState: PrayerFormContract.State,
        postInput: (PrayerFormContract.Inputs) -> Unit,
    ) {
        Column {
            Text("Prayer Form: PrayerId=${uiState.prayerId}")

            Button({ postInput(PrayerFormContract.Inputs.NavigateUp) }) {
                Text("Navigate Up")
            }
            Button({ postInput(PrayerFormContract.Inputs.GoBack) }) {
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
