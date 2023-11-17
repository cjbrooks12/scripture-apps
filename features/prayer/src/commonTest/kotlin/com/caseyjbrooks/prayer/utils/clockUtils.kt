package com.caseyjbrooks.prayer.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

public class TestClock(
    var millis: Long = 0L,
) : Clock {

    fun advanceTime() {
        millis++
    }

    override fun now(): Instant {
        return Instant.fromEpochMilliseconds(millis)
    }
}
