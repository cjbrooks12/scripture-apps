package com.caseyjbrooks.debug.screens.loglist

internal object LogListContract {
    internal data class State(
        val logFiles: List<String> = emptyList(),
    )

    internal sealed interface Inputs {
        data object Initialize : Inputs
        data object NavigateUp : Inputs
        data object GoBack : Inputs
    }

    internal sealed interface Events {
        data object NavigateUp : Events
    }
}
