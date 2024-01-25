package com.caseyjbrooks.prayer.screens.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.caseyjbrooks.ui.koin.LocalKoin
import com.copperleaf.ballast.repository.cache.getCachedOrEmptyList
import com.copperleaf.ballast.repository.cache.isLoading
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

public object PrayerListScreen {
    @Composable
    public fun Content() {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: PrayerListViewModel = remember(coroutineScope, koin) {
            koin.get(named("PrayerListViewModel")) { parametersOf(coroutineScope) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @Composable
    internal fun Content(
        uiState: PrayerListContract.State,
        postInput: (PrayerListContract.Inputs) -> Unit,
    ) {
        Column {
            Text("Prayer List")
            if (uiState.cachedPrayers.isLoading()) {
                CircularProgressIndicator()
            } else {
                Button({ postInput(PrayerListContract.Inputs.CreateNewPrayer) }) {
                    Text("New Prayer")
                }

                val prayerList = uiState.cachedPrayers.getCachedOrEmptyList()

                if (prayerList.isEmpty()) {
                    Text("No saved prayers")
                } else {
                    LazyColumn {
                        items(uiState.cachedPrayers.getCachedOrEmptyList()) { prayer ->
                            Box {
                                var showItemMenu by remember { mutableStateOf(false) }
                                ListItem(
                                    headlineContent = { Text(prayer.text.take(40)) },
                                    modifier = Modifier
                                        .clickable {
                                            postInput(PrayerListContract.Inputs.ViewPrayerDetails(prayer))
                                        },
                                    trailingContent = {
                                        IconButton({ showItemMenu = true }) {
                                            Icon(Icons.Default.MoreVert, "")
                                        }

                                        DropdownMenu(
                                            expanded = showItemMenu,
                                            onDismissRequest = { showItemMenu = false },
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("Edit") },
                                                onClick = { postInput(PrayerListContract.Inputs.EditPrayer(prayer)) },
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Pray Now") },
                                                onClick = { postInput(PrayerListContract.Inputs.PrayNow(prayer)) },
                                            )
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
