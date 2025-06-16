package com.copperleaf.ballast.queue

public fun interface InputId<Inputs : Any, Events : Any, State : Any> {
    public fun getId(input: Inputs): String
}
