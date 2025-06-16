@file:Suppress("UNCHECKED_CAST")

package com.copperleaf.ballast.queue

import kotlinx.datetime.Instant
import kotlin.time.Duration

data class SerializedJob(
    val queueName: String,
    val journeyId: String,
    val jobId: String,

    val payload: String,

    val priority: UInt,
    val insertedAt: Instant,
    val lastAttemptAt: Instant?,
    val delay: Duration,
    val attempts: UInt,
)
