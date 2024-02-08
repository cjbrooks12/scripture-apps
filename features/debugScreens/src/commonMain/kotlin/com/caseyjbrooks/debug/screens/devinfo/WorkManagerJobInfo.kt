package com.caseyjbrooks.debug.screens.devinfo

import kotlinx.datetime.Instant

internal data class WorkManagerJobInfo(
    val adapterClassName: String,
    val callbackClassName: String,
    val scheduleName: String,
    val withHistory: Boolean,
    val initialInstant: Instant?,
    val latestInstant: Instant?,
    val nextInstant: Instant?,
)

internal expect suspend fun getWorkManagerJobInfo(): List<WorkManagerJobInfo>

internal expect suspend fun testWorkManagerJob(info: WorkManagerJobInfo)
