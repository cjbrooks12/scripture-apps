package com.copperleaf.ballast.queue

public fun interface QueueThrottle {
    suspend fun awaitNext(emptyPollCount: Int)
}
