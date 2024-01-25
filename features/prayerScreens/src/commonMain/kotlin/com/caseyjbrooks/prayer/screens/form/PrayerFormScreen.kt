package com.caseyjbrooks.prayer.screens.form

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.ui.koin.LocalKoin
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

public object PrayerFormScreen {
    @Composable
    public fun Content(prayerId: PrayerId?) {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: PrayerFormViewModel = remember(coroutineScope, koin) {
            koin.get(named("PrayerFormViewModel")) { parametersOf(coroutineScope, prayerId) }
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
                val prayer = uiState.cachedPrayer.getCachedOrNull()

                val initialText = if (prayer != null) {
                    prayer.text
                } else {
                    ""
                }

                var prayerText: TextFieldValue by remember(initialText) { mutableStateOf(TextFieldValue(initialText)) }
                OutlinedTextField(
                    value = prayerText,
                    onValueChange = { prayerText = it },
                )

                if (prayer != null) {
                    Button(onClick = { postInput(PrayerFormContract.Inputs.UpdatePrayer(prayerText.text)) }) {
                        Text("Save")
                    }
                } else {
                    Button(onClick = { postInput(PrayerFormContract.Inputs.CreatePrayer(prayerText.text)) }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
