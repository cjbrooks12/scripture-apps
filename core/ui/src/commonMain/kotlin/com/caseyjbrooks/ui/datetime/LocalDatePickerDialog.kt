package com.caseyjbrooks.ui.datetime

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LocalDatePickerDialog(
    onDismissRequest: () -> Unit,
    initialDate: Instant?,
    onDateSelected: (Instant) -> Unit,
    timeZone: TimeZone,
) {
    LocalDatePickerDialog(
        onDismissRequest = onDismissRequest,
        initialDate = initialDate?.toLocalDateTime(timeZone)?.date,
        onDateSelected = { onDateSelected(it.atStartOfDayIn(timeZone)) },
        timeZone = timeZone
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LocalDatePickerDialog(
    onDismissRequest: () -> Unit,
    initialDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    timeZone: TimeZone,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate?.atStartOfDayIn(timeZone)?.toEpochMilliseconds()
    )
    DatePickerDialog(
        modifier = Modifier,
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button({
                val selectedLocalDateInTimeZone: LocalDate = datePickerState
                    .selectedDateMillis!!
                    .let {
                        val localDateInUtc = Instant
                            .fromEpochMilliseconds(it)
                            .toLocalDateTime(TimeZone.UTC)
                        LocalDate(
                            year = localDateInUtc.year,
                            month = localDateInUtc.month,
                            dayOfMonth = localDateInUtc.dayOfMonth,
                        )
                    }

                onDateSelected(selectedLocalDateInTimeZone)
            }) {
                Text("Ok")
            }
        },
        dismissButton = {
            Button(
                { onDismissRequest() },
                colors = ButtonDefaults.textButtonColors()
            ) {
                Text("Cancel")
            }
        },
    ) {
        DatePicker(datePickerState)
    }
}
