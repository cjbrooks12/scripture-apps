package com.copperleaf.ballast.queue.impl.timeout

import com.copperleaf.ballast.queue.InputTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class FixedInputTimeout<Inputs : Any, Events : Any, State : Any>(
    private val timeout: Duration = 30.seconds
) : InputTimeout<Inputs, Events, State> {
    override fun getTimeout(input: Inputs): Duration {
        return timeout
    }
}
