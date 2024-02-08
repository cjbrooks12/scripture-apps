package com.caseyjbrooks.debug.screens.devinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.ui.koin.LocalKoin
import kotlinx.datetime.Instant
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toLocalDateTime
import org.koin.core.parameter.parametersOf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.microseconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

@OptIn(ExperimentalMaterial3Api::class)
public object DeveloperInfoScreen {
    @Composable
    public fun Content() {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: DeveloperInfoViewModel = remember(coroutineScope, koin) {
            koin.get { parametersOf(coroutineScope) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Content(
        uiState: DeveloperInfoContract.State,
        postInput: (DeveloperInfoContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Developer Info") },
                navigationIcon = {
                    IconButton(onClick = { postInput(DeveloperInfoContract.Inputs.NavigateUp) }) {
                        Icon(Icons.Default.ArrowBack, "Navigate Up")
                    }
                },
                actions = {
                }
            )

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
                        Text(
                            "Basic Info",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 24.dp),
                        )

                        Text(
                            "App Version",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(uiState.appVersion)

                        Text(
                            "Git SHA",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(uiState.gitSha)

                        Text(
                            "Timezone",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text("${uiState.timeZone} ${uiState.timeZone.offsetAt(uiState.now)}")

                        Text(
                            "Now",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(uiState.now.displayDate(uiState))
                    }
                }

                Text(
                    "WorkManager Jobs",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                uiState.workManagerJobs.forEach { (jobInfo, inProgress) ->
                    Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                jobInfo.scheduleName,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            Text(
                                "${jobInfo.adapterClassName.substringAfterLast('.')} -> ${
                                    jobInfo.callbackClassName.substringAfterLast(
                                        '.'
                                    )
                                }",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(bottom = 24.dp),
                            )

                            if (jobInfo.initialInstant != null) {
                                Text(
                                    "Initial Instant",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    "${jobInfo.initialInstant.displayDate(uiState)} (${
                                        jobInfo.initialInstant.displayDuration(
                                            uiState
                                        )
                                    })"
                                )
                            }
                            if (jobInfo.latestInstant != null) {
                                Text(
                                    "Latest Instant",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    "${jobInfo.latestInstant.displayDate(uiState)} (${
                                        jobInfo.latestInstant.displayDuration(
                                            uiState
                                        )
                                    })"
                                )
                            }
                            if (jobInfo.nextInstant != null) {
                                Text(
                                    "Next Instant",
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                Text(
                                    "${jobInfo.nextInstant.displayDate(uiState)} (${
                                        jobInfo.nextInstant.displayDuration(
                                            uiState
                                        )
                                    })"
                                )
                            }

                            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.padding(top = 12.dp)) {
                                Button(
                                    onClick = { postInput(DeveloperInfoContract.Inputs.TestWorkManagerJob(jobInfo)) },
                                    enabled = !inProgress
                                ) {
                                    if (inProgress) {
                                        CircularProgressIndicator()
                                    } else {
                                        Text("Test now")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Instant.displayDate(
    uiState: DeveloperInfoContract.State,
): String {
    return with(this.toLocalDateTime(uiState.timeZone)) {
        "$dayOfWeek $month $dayOfMonth, $year at $hour:${minute.toString().padStart(2, '0')}:${
            second.toString().padStart(2, '0')
        }"
    }
}

private fun Instant.displayDuration(
    uiState: DeveloperInfoContract.State,
): String {
    return if (this > uiState.now) {
        // future instant
        with(this - uiState.now) {
            "${removeFraction(DurationUnit.SECONDS)} away"
        }
    } else {
        // past instant
        val duration = this - uiState.now
        with(uiState.now - this) {
            "${removeFraction(DurationUnit.SECONDS)} ago"
        }
    }
}

public fun Duration.removeFraction(minUnit: DurationUnit): Duration {
    return when (minUnit) {
        DurationUnit.NANOSECONDS -> this.inWholeNanoseconds.nanoseconds
        DurationUnit.MICROSECONDS -> this.inWholeMicroseconds.microseconds
        DurationUnit.MILLISECONDS -> this.inWholeMilliseconds.milliseconds
        DurationUnit.SECONDS -> this.inWholeSeconds.seconds
        DurationUnit.MINUTES -> this.inWholeMinutes.minutes
        DurationUnit.HOURS -> this.inWholeHours.hours
        DurationUnit.DAYS -> this.inWholeDays.days
        else -> this.inWholeSeconds.seconds
    }
}
