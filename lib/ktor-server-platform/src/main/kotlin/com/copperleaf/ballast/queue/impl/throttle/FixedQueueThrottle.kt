package com.copperleaf.ballast.queue.impl.throttle

import com.copperleaf.ballast.queue.QueueThrottle
import kotlinx.coroutines.delay
import kotlin.time.Duration

class FixedQueueThrottle(private val duration: Duration) : QueueThrottle {
    override suspend fun awaitNext(emptyPollCount: Int) {
        val delayAmount = if (emptyPollCount == 0) Duration.ZERO else duration
        delay(delayAmount)
    }
}
