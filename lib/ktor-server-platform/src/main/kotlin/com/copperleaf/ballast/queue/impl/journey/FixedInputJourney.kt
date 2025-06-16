package com.copperleaf.ballast.queue.impl.journey

import com.copperleaf.ballast.queue.InputJourney

class FixedInputJourney<Inputs : Any, Events : Any, State : Any>(
    private val id: String = "Default"
) : InputJourney<Inputs, Events, State> {
    override fun getJourneyId(input: Inputs): String {
        return id
    }
}
