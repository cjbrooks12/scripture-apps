package com.copperleaf.ballast.queue

import kotlin.time.Duration

public fun interface InputDelay<Inputs : Any, Events : Any, State : Any> {
    public fun inputDelay(input: Inputs): Duration
}
