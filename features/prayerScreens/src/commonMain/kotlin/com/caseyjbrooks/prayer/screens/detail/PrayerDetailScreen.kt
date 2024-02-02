package com.caseyjbrooks.prayer.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.caseyjbrooks.ui.koin.LocalKoin
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.isLoading
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
public object PrayerDetailScreen {
    @Composable
    public fun Content(prayerId: PrayerId) {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: PrayerDetailViewModel = remember(coroutineScope, koin) {
            koin.get { parametersOf(coroutineScope, prayerId) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Content(
        uiState: PrayerDetailContract.State,
        postInput: (PrayerDetailContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Prayer Details") },
                navigationIcon = {
                    IconButton(onClick = { postInput(PrayerDetailContract.Inputs.NavigateUp) }) {
                        Icon(Icons.Default.ArrowBack, "Navigate Up")
                    }
                },
                actions = {
                    IconButton(onClick = { postInput(PrayerDetailContract.Inputs.Edit) }) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                    IconButton(onClick = { postInput(PrayerDetailContract.Inputs.PrayNow) }) {
                        Icon(Icons.Default.Timer, "Pray Now")
                    }
                }
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
        uiState: PrayerDetailContract.State,
        postInput: (PrayerDetailContract.Inputs) -> Unit,
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
        uiState: PrayerDetailContract.State,
        postInput: (PrayerDetailContract.Inputs) -> Unit,
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
        uiState: PrayerDetailContract.State,
        postInput: (PrayerDetailContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val prayer = uiState.cachedPrayer.getCachedOrThrow()

            Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Prayer Text",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 24.dp),
                    )
                    Text(prayer.text)
                }
            }

            if (prayer.prayerType is SavedPrayerType.ScheduledCompletable) {
                Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Completion date",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 24.dp),
                        )
                        val date = (prayer.prayerType as SavedPrayerType.ScheduledCompletable)
                            .completionDate
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                        Text("Prayer will be archived after ${date.dayOfWeek.name}, ${date.month.name} ${date.dayOfMonth}, ${date.year}")
                    }
                }
            }

            if (prayer.tags.isNotEmpty()) {
                Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Tags",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 24.dp),
                        )

                        // (optional) tags
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            prayer.tags.forEach { tag ->
                                FilterChip(
                                    selected = false,
                                    label = { Text(tag.tag) },
                                    onClick = { },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
