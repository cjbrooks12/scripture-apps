package com.caseyjbrooks.debug.screens.logfile

internal object LogFileContract {
    internal data class State(
        val logFileName: String = "",
    )

    internal sealed interface Inputs {
        data class Initialize(val logFileName: String) : Inputs
        data object NavigateUp : Inputs
        data object GoBack : Inputs
    }

    internal sealed interface Events {
        data object NavigateUp : Events
    }
}
