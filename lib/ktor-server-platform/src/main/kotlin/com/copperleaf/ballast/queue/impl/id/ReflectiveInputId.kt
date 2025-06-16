package com.copperleaf.ballast.queue.impl.id

import com.copperleaf.ballast.queue.InputId

class ReflectiveInputId<Inputs : Any, Events : Any, State : Any> : InputId<Inputs, Events, State> {
    override fun getId(input: Inputs): String {
        return input::class.java.simpleName
    }
}
