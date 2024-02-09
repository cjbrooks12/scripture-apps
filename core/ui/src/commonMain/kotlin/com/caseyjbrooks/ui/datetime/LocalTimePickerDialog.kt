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
    val clock: Clock = LocalKoin.current.get()
    val currentTime: LocalTime = initialTime ?: run { clock.now().toLocalDateTime(timeZone).time }

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = false,
    )
    DatePickerDialog(
        modifier = Modifier,
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            Button({
                val selectedLocalTimeInTimeZone: LocalTime = LocalTime(
                    hour = timePickerState.hour,
                    minute = timePickerState.minute,
                    second = 0,
                    nanosecond = 0
                )

                onTimeSelected(selectedLocalTimeInTimeZone)
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
        TimePicker(timePickerState)
    }
}
