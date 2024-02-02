package com.caseyjbrooks.verses.screens.form

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.ui.koin.LocalKoin
import com.caseyjbrooks.ui.text.rememberLiveText
import com.caseyjbrooks.verses.models.VerseId
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getValueOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
public object VerseFormScreen {
    @Composable
    public fun Content(verseId: VerseId?) {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: VerseFormViewModel = remember(coroutineScope, koin) {
            koin.get { parametersOf(coroutineScope, verseId) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @Composable
    internal fun Content(
        uiState: VerseFormContract.State,
        postInput: (VerseFormContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    if (uiState.cachedVerse.getValueOrNull() == null) {
                        Text("New Verse")
                    } else {
                        Text("Edit Verse")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { postInput(VerseFormContract.Inputs.NavigateUp) }) {
                        Icon(Icons.Default.ArrowBack, "Navigate Up")
                    }
                },
                actions = {
                    IconButton(onClick = { postInput(VerseFormContract.Inputs.SaveVerse) }) {
                        Icon(Icons.Default.Save, "Save Verse")
                    }
                }
            )
            if (uiState.cachedVerse.isLoading()) {
                LoadingState(uiState, postInput)
            } else {
                ContentState(uiState, postInput)
            }
        }
    }

    @Composable
    internal fun ColumnScope.LoadingState(
        uiState: VerseFormContract.State,
        postInput: (VerseFormContract.Inputs) -> Unit,
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
    internal fun ColumnScope.ContentState(
        uiState: VerseFormContract.State,
        postInput: (VerseFormContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Reference", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "The Bible passage reference.",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 24.dp),
                    )

                    // main verse text
                    val (reference, setReference) = rememberLiveText(
                        initialValue = uiState.cachedVerse.getCachedOrNull()?.reference ?: "",
                        onTextChange = { postInput(VerseFormContract.Inputs.ReferenceUpdated(it)) }
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = reference,
                        onValueChange = setReference,
                        placeholder = { Text("e.g. John 3:16") },
                        label = { Text("Reference") }
                    )
                }
            }

            Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Verse Text", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "The Bible text to memorize.",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 24.dp),
                    )

                    // main verse text
                    val (verseText, setVerseText) = rememberLiveText(
                        initialValue = uiState.cachedVerse.getCachedOrNull()?.text ?: "",
                        onTextChange = { postInput(VerseFormContract.Inputs.VerseTextUpdated(it)) }
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = verseText,
                        onValueChange = setVerseText,
                        placeholder = { Text("Describe the verse") },
                        label = { Text("Verse Text") }
                    )
                }
            }

            Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Tags", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "Use tags to help categorize your verses",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 24.dp),
                    )

                    // (optional) tags
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        uiState.tags.forEach { tag ->
                            FilterChip(
                                selected = false,
                                trailingIcon = {
                                    IconButton({ postInput(VerseFormContract.Inputs.RemoveTag(tag)) }) {
                                        Icon(Icons.Default.Remove, "Remove tag")
                                    }
                                },
                                label = { Text(tag) },
                                onClick = { },
                            )
                        }
                    }

                    var addTagInputVisible by remember { mutableStateOf(false) }
                    if (!addTagInputVisible) {
                        Button(
                            onClick = { addTagInputVisible = true },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Add tags")
                        }
                    } else {
                        var tagTextFieldValue by remember { mutableStateOf(TextFieldValue()) }

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = tagTextFieldValue,
                            onValueChange = { tagTextFieldValue = it },
                            placeholder = { Text("Tag name") },
                            label = { Text("Tag") },
                            trailingIcon = {
                                IconButton(onClick = { postInput(VerseFormContract.Inputs.AddTag(tagTextFieldValue.text)) }) {
                                    Icon(Icons.Default.Add, "Add Tag")
                                }
                            }
                        )

                        Button(
                            onClick = { addTagInputVisible = false },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Done adding tags")
                        }
                    }
                }
            }
        }
    }
}
