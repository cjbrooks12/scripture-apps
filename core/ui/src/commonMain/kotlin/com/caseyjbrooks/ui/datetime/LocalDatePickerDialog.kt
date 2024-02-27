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
import com.caseyjbrooks.ui.koin.LocalKoin
import com.caseyjbrooks.ui.logging.LocalLogger
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@Composable
public fun LocalDatePickerDialog(
    onDismissRequest: () -> Unit,
    initialDate: Instant?,
    requireFutureDate: Boolean = false,
    onDateSelected: (Instant) -> Unit,
    timeZone: TimeZone,
) {
    val logger = LocalLogger.current.withTag("LocalDatePickerDialog1")

    LocalDatePickerDialog(
        onDismissRequest = {
            logger.d("tossing up onDismissRequest")
            onDismissRequest()
        },
        initialDate = initialDate?.toLocalDateTime(timeZone)?.date,
        requireFutureDate = requireFutureDate,
        onDateSelected = {
            logger.d("tossing up onDateSelected")
            onDateSelected(it.atStartOfDayIn(timeZone))
        },
        timeZone = timeZone
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LocalDatePickerDialog(
    onDismissRequest: () -> Unit,
    initialDate: LocalDate?,
    requireFutureDate: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    timeZone: TimeZone,
) {
    val logger = LocalLogger.current.withTag("LocalDatePickerDialog2")
    val clock: Clock = LocalKoin.current.get()
    val currentDate = clock.now().toLocalDateTime(timeZone).date

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate?.atStartOfDayIn(timeZone)?.toEpochMilliseconds()
    )

    val selectedDateIsValid = datePickerState
        .selectedDateMillis
        ?.let { selectedTimeMillis ->
            val localDateInUtc = Instant
                .fromEpochMilliseconds(selectedTimeMillis)
                .toLocalDateTime(TimeZone.UTC)

            val selectedDate = LocalDate(
                year = localDateInUtc.year,
                month = localDateInUtc.month,
                dayOfMonth = localDateInUtc.dayOfMonth,
            )

            if (requireFutureDate) {
                selectedDate > currentDate
            } else {
                true
            }
        }
        ?: false

    val postUpdateToLocalDate = {
        if (selectedDateIsValid) {
            datePickerState
                .selectedDateMillis!!
                .let { selectedTimeMillis ->
                    val localDateInUtc = Instant
                        .fromEpochMilliseconds(selectedTimeMillis)
                        .toLocalDateTime(TimeZone.UTC)

                    val selectedDate = LocalDate(
                        year = localDateInUtc.year,
                        month = localDateInUtc.month,
                        dayOfMonth = localDateInUtc.dayOfMonth,
                    )
                    onDateSelected(selectedDate)
                }
        } else {
            onDismissRequest()
        }
    }

    DatePickerDialog(
        modifier = Modifier,
        onDismissRequest = {
            logger.d("Dialog dismissed")
            onDismissRequest()
        },
        confirmButton = {
            Button(
                enabled = selectedDateIsValid,
                onClick = {
                    logger.d("OK button clicked")
                    postUpdateToLocalDate()
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    logger.d("Cancel button clicked")
                    onDismissRequest()
                },
                colors = ButtonDefaults.textButtonColors()
            ) {
                Text("Cancel")
            }
        },
        content = { DatePicker(datePickerState) },
    )
}
