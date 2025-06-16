package com.copperleaf.ballast.queue.impl.id

import com.copperleaf.ballast.queue.InputId

class FixedInputId<Inputs : Any, Events : Any, State : Any>(
    private val id: String
) : InputId<Inputs, Events, State> {
    override fun getId(input: Inputs): String {
        return id
    }
}
