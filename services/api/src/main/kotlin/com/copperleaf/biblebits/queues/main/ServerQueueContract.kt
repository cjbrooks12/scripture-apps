package com.copperleaf.biblebits.queues.main

import kotlinx.serialization.Serializable
import kotlin.time.Duration

object ServerQueueContract {
    @Serializable
    data class State(
        val count: Int = 0,
    )

    @Serializable
    sealed interface Inputs {
        @Serializable
        data object Increment : Inputs
        @Serializable
        data object Fail : Inputs
        @Serializable
        data class Delayed(val delay: Duration) : Inputs
        @Serializable
        data class Timeout(val duration: Duration) : Inputs
    }

    sealed interface Events {
    }
}
