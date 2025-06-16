package com.copperleaf.ballast.queue.impl.retry

import com.copperleaf.ballast.queue.RetryStrategy

class FixedRetryStrategy<Inputs : Any, Events : Any, State : Any>(
    private val maxAttempts: UInt = 3u
) : RetryStrategy<Inputs, Events, State> {
    override fun shouldRetry(input: Inputs, numberOfAttempts: UInt): Boolean {
        return numberOfAttempts < maxAttempts
    }
}
