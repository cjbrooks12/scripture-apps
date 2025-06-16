package com.copperleaf.ballast.queue

public fun interface InputJourney<Inputs : Any, Events : Any, State : Any> {
    public fun getJourneyId(input: Inputs): String
}
