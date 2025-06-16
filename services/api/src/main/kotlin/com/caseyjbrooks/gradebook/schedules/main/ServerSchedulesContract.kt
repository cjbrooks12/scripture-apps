package com.caseyjbrooks.gradebook.schedules.main

object ServerSchedulesContract {
    data object State

    sealed interface Inputs {
        data class CleanCache(val key: String) : Inputs
    }

    sealed interface Events {
    }
}
