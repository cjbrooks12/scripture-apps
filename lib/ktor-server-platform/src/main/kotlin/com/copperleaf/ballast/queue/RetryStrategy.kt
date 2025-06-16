package com.copperleaf.ballast.queue

public fun interface RetryStrategy<Inputs : Any, Events : Any, State : Any> {
    public fun shouldRetry(input: Inputs, numberOfAttempts: UInt): Boolean
}
