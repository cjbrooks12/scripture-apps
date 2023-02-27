package com.caseyjbrooks.scripturenow.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.RadioButton
import androidx.compose.material.Switch
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
                            checked = state.showMainVerse,
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
                                    selected = state.verseOfTheDayService == service,
                                    onClick = null,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
