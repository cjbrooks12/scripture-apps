package com.copperleaf.beer.main

import com.copperleaf.beer.models.ResourceValue
import kotlinx.serialization.Serializable

object MainContract {

    @Serializable
    data class State(
        val grain: ResourceValue = ResourceValue(0),
        val malt: ResourceValue = ResourceValue(0),
        val wort: ResourceValue = ResourceValue(0),
        val beer: ResourceValue = ResourceValue(0),
        val bottles: ResourceValue = ResourceValue(0),
        val sold: ResourceValue = ResourceValue(0),
        val money: ResourceValue = ResourceValue(100),
    ) {

    }

    sealed class Inputs {
        class UpdateState(val fn: (State) -> State) : Inputs()
    }

    sealed class Events {

    }
}
