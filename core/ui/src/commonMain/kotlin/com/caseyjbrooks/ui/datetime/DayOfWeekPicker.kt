package com.caseyjbrooks.ui.datetime

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DayOfWeek

@Composable
public fun DayOfWeekPicker(
    daysOfWeek: Set<DayOfWeek>,
    onDaysOfWeekUpdated: (Set<DayOfWeek>) -> Unit,
    modifier: Modifier = Modifier,
    startOnSunday: Boolean = true
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        val allDaysOfWeek = if (startOnSunday) {
            listOf(
                DayOfWeek.SUNDAY,
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
            )
        } else {
            listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY,
            )
        }

        allDaysOfWeek.forEach { dayOfWeek ->
            val isSelected = dayOfWeek in daysOfWeek
            val displayText = when (dayOfWeek) {
                DayOfWeek.SUNDAY -> "Su"
                DayOfWeek.MONDAY -> "Mo"
                DayOfWeek.TUESDAY -> "Tu"
                DayOfWeek.WEDNESDAY -> "We"
                DayOfWeek.THURSDAY -> "Th"
                DayOfWeek.FRIDAY -> "Fr"
                DayOfWeek.SATURDAY -> "Sa"
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clickable {
                        if (isSelected) {
                            onDaysOfWeekUpdated(daysOfWeek - dayOfWeek)
                        } else {
                            onDaysOfWeekUpdated(daysOfWeek + dayOfWeek)
                        }
                    }
                    .then(
                        if (isSelected) {
                            Modifier.border(1.dp, MaterialTheme.colorScheme.primary)
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = displayText,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}
