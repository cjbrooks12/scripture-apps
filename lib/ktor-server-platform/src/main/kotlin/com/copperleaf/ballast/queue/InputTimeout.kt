package com.copperleaf.ballast.queue

import kotlin.time.Duration

public fun interface InputTimeout<Inputs : Any, Events : Any, State : Any> {

    public fun getTimeout(input: Inputs): Duration
}
