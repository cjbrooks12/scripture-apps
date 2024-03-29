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
import androidx.compose.material3.Divider
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
import com.caseyjbrooks.prayer.models.PrayerId
import com.caseyjbrooks.prayer.models.PrayerNotification
import com.caseyjbrooks.ui.datetime.DayOfWeekPicker
import com.caseyjbrooks.ui.datetime.LocalDatePickerDialog
import com.caseyjbrooks.ui.datetime.LocalDateTimePickerDialog
import com.caseyjbrooks.ui.datetime.LocalTimePickerDialog
import com.caseyjbrooks.ui.koin.LocalKoin
import com.caseyjbrooks.ui.logging.LocalLogger
import com.caseyjbrooks.ui.text.rememberLiveText
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getValueOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.TimeZone
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
        val timeZone: TimeZone = LocalKoin.current.get()

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
                    CompletionDate(timeZone, uiState, postInput)
                }
            }

            Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Notifications", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "A schedule to show notifications for this prayer.",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 24.dp),
                    )

                    when (uiState.notification) {
                        is PrayerNotification.None -> {
                            NotificationScheduleNone(timeZone, uiState.notification, uiState, postInput)
                        }

                        is PrayerNotification.Once -> {
                            NotificationScheduleOnce(timeZone, uiState.notification, uiState, postInput)
                        }

                        is PrayerNotification.Daily -> {
                            NotificationScheduleDaily(timeZone, uiState.notification, uiState, postInput)
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

    @Composable
    private fun CompletionDate(
        timeZone: TimeZone,
        uiState: PrayerFormContract.State,
        postInput: (PrayerFormContract.Inputs) -> Unit,
    ) {
        val logger = LocalLogger.current.withTag("PrayerFormScreen")

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
            val date = uiState.completionDate.toLocalDateTime(timeZone).date
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
            LocalDatePickerDialog(
                onDismissRequest = {
                    logger.d("LocalDatePickerDialog onDismissRequest()")
                    datePickerDialogVisible = false
                },
                initialDate = uiState.completionDate,
                requireFutureDate = true,
                onDateSelected = {
                    logger.d("LocalDatePickerDialog onDateSelected()")
                    postInput(PrayerFormContract.Inputs.CompletionDateUpdated(it))
                },
                timeZone = timeZone,
            )
        }
    }

    @Composable
    private fun NotificationScheduleNone(
        timeZone: TimeZone,
        dailyNotification: PrayerNotification.None,
        uiState: PrayerFormContract.State,
        postInput: (PrayerFormContract.Inputs) -> Unit,
    ) {
        Text("No notifications will be shown for this prayer.")

        Divider()
        var showOneTimeNotificationPicker by remember { mutableStateOf(false) }
        var showDailyNotificationPicker by remember { mutableStateOf(false) }
        Button({ showOneTimeNotificationPicker = true }) {
            Text("Notify once")
        }
        Button({ showDailyNotificationPicker = true }) {
            Text("Set daily notification")
        }

        if (showOneTimeNotificationPicker) {
            LocalDateTimePickerDialog(
                onDismissRequest = { showOneTimeNotificationPicker = false },
                initialInstant = null,
                onInstantSelected = { selectedTime ->
                    postInput(
                        PrayerFormContract.Inputs.PrayerNotificationUpdated(
                            PrayerNotification.Once(
                                selectedTime
                            )
                        )
                    )
                    showOneTimeNotificationPicker = false
                },
                timeZone = timeZone,
            )
        }
        if (showDailyNotificationPicker) {
            LocalTimePickerDialog(
                onDismissRequest = { showDailyNotificationPicker = false },
                initialTime = null,
                onTimeSelected = { selectedTime ->
                    postInput(
                        PrayerFormContract.Inputs.PrayerNotificationUpdated(
                            PrayerNotification.Daily(
                                DayOfWeek.entries.toSet(),
                                selectedTime
                            )
                        )
                    )
                    showDailyNotificationPicker = false
                },
                timeZone = timeZone,
            )
        }
    }

    @Composable
    private fun NotificationScheduleOnce(
        timeZone: TimeZone,
        dailyNotification: PrayerNotification.Once,
        uiState: PrayerFormContract.State,
        postInput: (PrayerFormContract.Inputs) -> Unit,
    ) {
        val dateTime = dailyNotification.instant.toLocalDateTime(timeZone)
        Text("A notification will be shown on ${dateTime.date} at ${dateTime.time}")

        Divider()
        var showOneTimeNotificationPicker by remember { mutableStateOf(false) }
        var showDailyNotificationPicker by remember { mutableStateOf(false) }
        Button({
            postInput(
                PrayerFormContract.Inputs.PrayerNotificationUpdated(
                    PrayerNotification.None
                )
            )
        }) {
            Text("Remove notifications")
        }
        Button({ showOneTimeNotificationPicker = true }) {
            Text("Update one-time notification")
        }
        Button({ showDailyNotificationPicker = true }) {
            Text("Change to daily notification")
        }

        if (showOneTimeNotificationPicker) {
            LocalDateTimePickerDialog(
                onDismissRequest = { showOneTimeNotificationPicker = false },
                initialInstant = dailyNotification.instant,
                onInstantSelected = { selectedTime ->
                    postInput(
                        PrayerFormContract.Inputs.PrayerNotificationUpdated(
                            PrayerNotification.Once(
                                selectedTime
                            )
                        )
                    )
                    showOneTimeNotificationPicker = false
                },
                timeZone = timeZone,
            )
        }
        if (showDailyNotificationPicker) {
            LocalTimePickerDialog(
                onDismissRequest = { showDailyNotificationPicker = false },
                initialTime = null,
                onTimeSelected = { selectedTime ->
                    postInput(
                        PrayerFormContract.Inputs.PrayerNotificationUpdated(
                            PrayerNotification.Daily(
                                DayOfWeek.entries.toSet(),
                                selectedTime
                            )
                        )
                    )
                    showDailyNotificationPicker = false
                },
                timeZone = timeZone,
            )
        }
    }

    @Composable
    private fun NotificationScheduleDaily(
        timeZone: TimeZone,
        dailyNotification: PrayerNotification.Daily,
        uiState: PrayerFormContract.State,
        postInput: (PrayerFormContract.Inputs) -> Unit,
    ) {
        val isWeekDays = dailyNotification.daysOfWeek == setOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
        )
        val isWeekends = dailyNotification.daysOfWeek == setOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.SATURDAY,
        )

        if (isWeekDays) {
            Text("Notifications will be shown weekdays at ${dailyNotification.time}")
        } else if (isWeekends) {
            Text("Notifications will be shown weekends at ${dailyNotification.time}")
        } else {
            Text("Notifications will be at ${dailyNotification.time} on ${dailyNotification.daysOfWeek}")
        }

        Divider()
        var showOneTimeNotificationPicker by remember { mutableStateOf(false) }
        var showDailyNotificationPicker by remember { mutableStateOf(false) }
        Button({
            postInput(
                PrayerFormContract.Inputs.PrayerNotificationUpdated(
                    PrayerNotification.None
                )
            )
        }) {
            Text("Remove notifications")
        }
        Button({ showOneTimeNotificationPicker = true }) {
            Text("Change to one-time notification")
        }
        Button({ showDailyNotificationPicker = true }) {
            Text("Change time")
        }

        DayOfWeekPicker(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            daysOfWeek = dailyNotification.daysOfWeek,
            onDaysOfWeekUpdated = { daysOfWeek ->
                postInput(
                    PrayerFormContract.Inputs.PrayerNotificationUpdated(
                        dailyNotification.copy(daysOfWeek = daysOfWeek)
                    )
                )
            },
        )

        if (showOneTimeNotificationPicker) {
            LocalDateTimePickerDialog(
                onDismissRequest = { showOneTimeNotificationPicker = false },
                initialInstant = null,
                onInstantSelected = { selectedTime ->
                    postInput(
                        PrayerFormContract.Inputs.PrayerNotificationUpdated(
                            PrayerNotification.Once(
                                selectedTime
                            )
                        )
                    )
                    showOneTimeNotificationPicker = false
                },
                timeZone = timeZone,
            )
        }
        if (showDailyNotificationPicker) {
            LocalTimePickerDialog(
                onDismissRequest = { showDailyNotificationPicker = false },
                initialTime = null,
                onTimeSelected = { selectedTime ->
                    postInput(
                        PrayerFormContract.Inputs.PrayerNotificationUpdated(
                            dailyNotification.copy(time = selectedTime)
                        )
                    )
                    showDailyNotificationPicker = false
                },
                timeZone = timeZone,
            )
        }
    }
}
