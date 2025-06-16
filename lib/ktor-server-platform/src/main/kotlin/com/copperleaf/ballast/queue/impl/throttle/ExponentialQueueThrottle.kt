package com.copperleaf.ballast.queue.impl.throttle

import com.copperleaf.ballast.queue.QueueThrottle
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ExponentialQueueThrottle(
    private val factor: Double = 2.0,
    private val unit: DurationUnit = DurationUnit.SECONDS,
    private val max: Duration = 1.minutes
) : QueueThrottle {
    override suspend fun awaitNext(emptyPollCount: Int) {
        val delayAmount = if (emptyPollCount == 0) {
            Duration.ZERO
        } else {
            val computedExponentialBackoff = factor.pow(emptyPollCount).toDuration(unit)
            minOf(computedExponentialBackoff, max)
        }
        delay(delayAmount)
    }
}
