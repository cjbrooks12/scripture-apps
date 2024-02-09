package com.caseyjbrooks.ui.datetime

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LocalDateTimePickerDialog(
    onDismissRequest: () -> Unit,
    initialInstant: Instant?,
    onInstantSelected: (Instant) -> Unit,
    timeZone: TimeZone,
) {
    LocalDateTimePickerDialog(
        onDismissRequest = onDismissRequest,
        initialDateTime = initialInstant?.toLocalDateTime(timeZone),
        onDateTimeSelected = { onInstantSelected(it.toInstant(timeZone)) },
        timeZone = timeZone
    )
}

private enum class LocalDateTimePickerDialogStep {
    DateStep,
    TimeStep,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LocalDateTimePickerDialog(
    onDismissRequest: () -> Unit,
    initialDateTime: LocalDateTime?,
    onDateTimeSelected: (LocalDateTime) -> Unit,
    timeZone: TimeZone,
) {
    var selectedDate: LocalDate? by remember { mutableStateOf(initialDateTime?.date) }
    var selectedTime: LocalTime? by remember { mutableStateOf(initialDateTime?.time) }
    var step: LocalDateTimePickerDialogStep by remember { mutableStateOf(LocalDateTimePickerDialogStep.DateStep) }

    when (step) {
        LocalDateTimePickerDialogStep.DateStep -> {
            LocalDatePickerDialog(
                onDismissRequest = onDismissRequest,
                initialDate = selectedDate,
                onDateSelected = {
                    selectedDate = it
                    step = LocalDateTimePickerDialogStep.TimeStep
                },
                timeZone = timeZone,
            )
        }

        LocalDateTimePickerDialogStep.TimeStep -> {
            LocalTimePickerDialog(
                onDismissRequest = onDismissRequest,
                initialTime = selectedTime,
                onTimeSelected = {
                    selectedTime = it
                    if (selectedDate != null) {
                        onDateTimeSelected(
                            LocalDateTime(selectedDate!!, selectedTime!!)
                        )
                    }
                },
                timeZone = timeZone,
            )
        }
    }
}
