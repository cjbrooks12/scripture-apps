package com.copperleaf.ballast.queue

public fun interface DeadLetterQueue<Inputs : Any, Events : Any, State : Any> {
    public suspend fun inputDied(input: Inputs)
}
