package com.caseyjbrooks.foryou.ui.dashboard

internal object ForYouDashboardContract {
    internal data class State(
        val loading: Boolean = false,
    )

    internal sealed interface Inputs {
        data object Initialize : Inputs
        data object GoBack : Inputs
    }

    internal sealed interface Events {
        data object NavigateUp : Events
    }
}
