package com.copperleaf.ballast.queue.impl.priority

import com.copperleaf.ballast.queue.InputPriority

class FixedInputPriority<Inputs : Any, Events : Any, State : Any>(
    private val priority: UInt = 0u
) : InputPriority<Inputs, Events, State> {
    override fun assignPriority(input: Inputs): UInt {
        return priority
    }
}
