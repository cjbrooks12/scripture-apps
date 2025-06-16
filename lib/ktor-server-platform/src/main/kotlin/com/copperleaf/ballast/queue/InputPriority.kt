package com.copperleaf.ballast.queue

public fun interface InputPriority<Inputs : Any, Events : Any, State : Any> {

    /**
     * Assign a priority to the Input
     */
    public fun assignPriority(input: Inputs): UInt
}
