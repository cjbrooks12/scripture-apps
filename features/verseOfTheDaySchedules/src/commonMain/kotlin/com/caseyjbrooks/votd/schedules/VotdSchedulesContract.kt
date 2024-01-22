package com.caseyjbrooks.votd.schedules

internal object VotdSchedulesContract {
    internal data class State(
        val loading: Boolean = false,
    )

    internal sealed interface Inputs {
        data object FetchVotd : Inputs
        data object VotdNotification : Inputs
    }

    internal sealed interface Events {
    }
}
