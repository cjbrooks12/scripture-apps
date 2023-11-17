package com.caseyjbrooks.prayer.ui.detail

internal object PrayerDetailContract {
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
