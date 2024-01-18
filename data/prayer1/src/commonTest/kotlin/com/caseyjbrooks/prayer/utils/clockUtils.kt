package com.caseyjbrooks.prayer.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

public class TestClock(
    var millis: Long = 0L,
) : Clock {

    fun advanceTime() {
        millis++
    }

    fun advanceTimeBy(duration: Duration) {
        millis += duration.inWholeMilliseconds
    }

    override fun now(): Instant {
        return Instant.fromEpochMilliseconds(millis)
    }
}
