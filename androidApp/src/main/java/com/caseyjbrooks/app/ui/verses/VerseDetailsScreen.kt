package com.caseyjbrooks.app.ui.verses

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.uuidFrom
import com.caseyjbrooks.app.utils.ComposeScreen
import com.caseyjbrooks.app.utils.theme.LocalInjector
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.verses.detail.MemoryVerseDetailsContract

class VerseDetailsScreen : ComposeScreen(Destinations.App.Verses.Detail) {

    @Composable
    override fun screenContent(destination: Destination): Content {
        val coroutineScope = rememberCoroutineScope()
        val injector = LocalInjector.current
        val vm = remember(coroutineScope, injector) { injector.verseDetailsViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()

        val uuid = uuidFrom(destination.pathParameters["verseId"]!!.single())
        LaunchedEffect(vm, uuid) {
            vm.trySend(MemoryVerseDetailsContract.Inputs.Initialize(uuid))
        }

        return rememberHomescreenContent(
            appBarContent = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { vm.trySend(MemoryVerseDetailsContract.Inputs.GoBack) }) {
                            Icon(Icons.Default.ArrowBack, "Go Back")
                        }
                    },
                    title = { Text(vmState.memoryVerse.getCachedOrNull()?.reference ?: "") },
                    actions = {
                        IconButton(onClick = { vm.trySend(MemoryVerseDetailsContract.Inputs.EditVerse) }) {
                            Icon(Icons.Default.Edit, "Edit verse")
                        }
                    }
                )
            },
            mainContent = {
                vmState.memoryVerse.getCachedOrNull()?.let { verse ->
                    Text(
                        text = verse.reference,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = verse.text,
                        style = MaterialTheme.typography.body1
                    )
                    Spacer(Modifier.height(16.dp))
                    if (verse.notice.isNotBlank()) {
                        Text(
                            text = verse.reference,
                            style = MaterialTheme.typography.body1,
                            fontStyle = FontStyle.Italic,
                        )
                    }
//                            Spacer(Modifier.weight(1f))

                    var showConfirmationPopup by remember { mutableStateOf(false) }

                    if (showConfirmationPopup) {
                        AlertDialog(
                            onDismissRequest = { showConfirmationPopup = false },
                            text = { Text("Delete ${verse.reference}?") },
                            confirmButton = {
                                TextButton(onClick = {
                                    vm.trySend(MemoryVerseDetailsContract.Inputs.DeleteVerse)
                                    showConfirmationPopup = false
                                }) {
                                    Text("Delete")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showConfirmationPopup = false
                                }) {
                                    Text("Cancel")
                                }
                            },
                        )
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showConfirmationPopup = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
                    ) {
                        Text("Delete")
                    }
                }
            }
        )
    }
}
