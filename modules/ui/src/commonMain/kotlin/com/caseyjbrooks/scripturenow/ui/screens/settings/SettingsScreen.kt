package com.caseyjbrooks.scripturenow.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.BottomBarLayout
import com.caseyjbrooks.scripturenow.ui.layouts.ScrollableContent
import com.caseyjbrooks.scripturenow.viewmodel.settings.SettingsContract

@Composable
public fun SettingsScreen() {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) { injector.getSettingsViewModel(coroutineScope) }
    val vmState by vm.observeStates().collectAsState()

    SettingsScreen(vmState) { vm.trySend(it) }
}

@Composable
public fun SettingsScreen(
    state: SettingsContract.State,
    postInput: (SettingsContract.Inputs) -> Unit,
) {
    BottomBarLayout(
        title = { Text("Settings") },
    ) {
        ScrollableContent {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .clickable { postInput(SettingsContract.Inputs.ToggleShowMainVerse) }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Show Main Verse")

                        Switch(
                            checked = state.globalState.appPreferences.showMainVerse,
                            onCheckedChange = null
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .clickable { postInput(SettingsContract.Inputs.ToggleShowMainVerse) }
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Verse of the Day Service")
                    Column(modifier = Modifier.fillMaxWidth().padding(start = 8.dp)) {
                        VerseOfTheDayService.values().forEach { service ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        postInput(SettingsContract.Inputs.SetVerseOfTheDayServicePreference(service))
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(service.name)

                                RadioButton(
                                    selected = state.globalState.appPreferences.verseOfTheDayService == service,
                                    onClick = null,
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .then(
                        if (state.updateStatus != SettingsContract.UpdateStatus.NoneAvailable) {
                            Modifier.clickable { postInput(SettingsContract.Inputs.UpdateAppButtonClicked) }
                        } else {
                            Modifier
                        }
                    )
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("About")

                    Text("App Version: ${state.currentVersion}")
                    when (state.updateStatus) {
                        SettingsContract.UpdateStatus.NoneAvailable -> {
                            Text("You are on the latest version")
                        }

                        SettingsContract.UpdateStatus.UpdateAvailable -> {
                            Text("An update is available. Latest version is ${state.latestVersion}")
                        }

                        SettingsContract.UpdateStatus.UpdateRequired -> {
                            Text("An update is required. Minimum version is ${state.minVersion}, the latest version ${state.latestVersion}")
                        }
                    }

                    Button(onClick = { postInput(SettingsContract.Inputs.CheckForUpdates) }) {
                        Text("Check for updates")
                    }
                }
            }
        }
    }
}
