package com.caseyjbrooks.debug.screens.devinfo

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

internal object DeveloperInfoContract {
    internal data class State(
        val timeZone: TimeZone = TimeZone.UTC,
        val clock: Clock = Clock.System,
        val now: Instant = clock.now(),

        val appVersion: String = "",
        val gitSha: String = "",
        val workManagerJobs: List<Pair<WorkManagerJobInfo, Boolean>> = emptyList()
    )

    internal sealed interface Inputs {
        data object Initialize : Inputs
        data class TestWorkManagerJob(val info: WorkManagerJobInfo) : Inputs
        data object UpdateNow : Inputs
        data object NavigateUp : Inputs
    }

    internal sealed interface Events {
        data object NavigateUp : Events
    }
}
