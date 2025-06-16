package com.copperleaf.ballast.queue.impl.delay

import com.copperleaf.ballast.queue.InputDelay
import kotlin.time.Duration

class FixedInputDelay<Inputs : Any, Events : Any, State : Any>(
    private val delay: Duration = Duration.ZERO
) : InputDelay<Inputs, Events, State> {
    override fun inputDelay(input: Inputs): Duration {
        return delay
    }
}
