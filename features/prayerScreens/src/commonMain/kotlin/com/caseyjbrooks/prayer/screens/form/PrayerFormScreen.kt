package com.caseyjbrooks.prayer.screens.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
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
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.ui.koin.LocalKoin
import com.caseyjbrooks.ui.text.rememberLiveText
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getValueOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
public object PrayerFormScreen {
    @Composable
    public fun Content(prayerId: PrayerId?) {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: PrayerFormViewModel = remember(coroutineScope, koin) {
            koin.get { parametersOf(coroutineScope, prayerId) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @Composable
    internal fun Content(
        uiState: PrayerFormContract.State,
        postInput: (PrayerFormContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    if (uiState.cachedPrayer.getValueOrNull() == null) {
                        Text("New Prayer")
                    } else {
                        Text("Edit Prayer")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { postInput(PrayerFormContract.Inputs.NavigateUp) }) {
                        Icon(Icons.Default.ArrowBack, "Navigate Up")
                    }
                },
                actions = {
                    IconButton(onClick = { postInput(PrayerFormContract.Inputs.SavePrayer) }) {
                        Icon(Icons.Default.Save, "Save Prayer")
                    }
                }
            )
            if (uiState.cachedPrayer.isLoading()) {
                LoadingState(uiState, postInput)
            } else {
                ContentState(uiState, postInput)
            }
        }
    }

    @Composable
    internal fun ColumnScope.LoadingState(
        uiState: PrayerFormContract.State,
        postInput: (PrayerFormContract.Inputs) -> Unit,
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
        uiState: PrayerFormContract.State,
        postInput: (PrayerFormContract.Inputs) -> Unit,
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
                    Text("Prayer Text", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "Describe who or what you want to be praying for.",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 24.dp),
                    )

                    // main prayer text
                    val (prayerText, setPrayerText) = rememberLiveText(
                        initialValue = uiState.cachedPrayer.getCachedOrNull()?.text ?: "",
                        onTextChange = { postInput(PrayerFormContract.Inputs.PrayerTextUpdated(it)) }
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = prayerText,
                        onValueChange = setPrayerText,
                        placeholder = { Text("Describe the prayer") },
                        label = { Text("Prayer Text") }
                    )
                }
            }

            Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Completion date", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "If a completion date it set, it will be automatically moved to the archive after that date.",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 24.dp),
                    )

                    // (optional) completion date
                    var datePickerDialogVisible by remember { mutableStateOf(false) }
                    if (uiState.completionDate == null) {
                        Button(
                            onClick = { datePickerDialogVisible = true },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Add completion Date")
                        }
                    } else {
                        val date = uiState.completionDate.toLocalDateTime(TimeZone.currentSystemDefault()).date
                        Text("Prayer will be archived after ${date.dayOfWeek.name}, ${date.month.name} ${date.dayOfMonth}, ${date.year}")
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { postInput(PrayerFormContract.Inputs.CompletionDateUpdated(null)) },
                                colors = ButtonDefaults.outlinedButtonColors(),
                                modifier = Modifier.weight(1f),
                            ) {
                                Text("Remove completion Date")
                            }
                            Button(
                                onClick = { datePickerDialogVisible = true },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text("Change completion Date")
                            }
                        }
                    }

                    if (datePickerDialogVisible) {
                        val datePickerState = rememberDatePickerState(
                            initialSelectedDateMillis = uiState.completionDate?.toEpochMilliseconds()
                        )
                        DatePickerDialog(
                            onDismissRequest = { datePickerDialogVisible = false },
                            confirmButton = {
                                Button({
                                    val equivalentInstantInLocalTimeZone: Instant? =
                                        datePickerState.selectedDateMillis?.let {
                                            val localDateInUtc =
                                                Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.UTC)
                                            LocalDateTime(
                                                year = localDateInUtc.year,
                                                month = localDateInUtc.month,
                                                dayOfMonth = localDateInUtc.dayOfMonth,
                                                hour = localDateInUtc.hour,
                                                minute = localDateInUtc.minute,
                                                second = 0,
                                                nanosecond = 0,
                                            ).toInstant(TimeZone.currentSystemDefault())
                                        }

                                    postInput(
                                        PrayerFormContract.Inputs.CompletionDateUpdated(equivalentInstantInLocalTimeZone)
                                    )
                                    datePickerDialogVisible = false
                                }) {
                                    Text("Ok")
                                }
                            },
                            modifier = Modifier,
                            dismissButton = {
                                Button(
                                    { datePickerDialogVisible = false },
                                    colors = ButtonDefaults.textButtonColors()
                                ) {
                                    Text("Cancel")
                                }
                            },
                        ) {
                            DatePicker(datePickerState)
                        }
                    }
                }
            }

            Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Tags", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "Use tags to help categorize your prayers",
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
                                    IconButton({ postInput(PrayerFormContract.Inputs.RemoveTag(tag)) }) {
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
                                IconButton(onClick = { postInput(PrayerFormContract.Inputs.AddTag(tagTextFieldValue.text)) }) {
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
