package com.caseyjbrooks.ui.datetime

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.caseyjbrooks.ui.koin.LocalKoin
import com.caseyjbrooks.ui.logging.LocalLogger
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun LocalTimePickerDialog(
    onDismissRequest: () -> Unit,
    initialTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    timeZone: TimeZone,
) {
    val logger = LocalLogger.current.withTag("LocalTimePickerDialog1")

    val clock: Clock = LocalKoin.current.get()
    val currentTime: LocalTime = initialTime ?: run { clock.now().toLocalDateTime(timeZone).time }

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = false,
    )
    DatePickerDialog(
        modifier = Modifier,
        onDismissRequest = {
            logger.d("dialog dismissed")
            onDismissRequest()
        },
        confirmButton = {
            Button(
                onClick = {
                    logger.d("Ok button clicked")
                    val selectedLocalTimeInTimeZone: LocalTime = LocalTime(
                        hour = timePickerState.hour,
                        minute = timePickerState.minute,
                        second = 0,
                        nanosecond = 0
                    )

                    onTimeSelected(selectedLocalTimeInTimeZone)
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
    ) {
        TimePicker(timePickerState)
    }
}
