package com.copperleaf.ballast.queue.impl.dlq

import com.copperleaf.ballast.queue.DeadLetterQueue

class LogDeadLetterQueue<Inputs : Any, Events : Any, State : Any> : DeadLetterQueue<Inputs, Events, State> {
    override suspend fun inputDied(input: Inputs) {
        println(input)
    }
}
