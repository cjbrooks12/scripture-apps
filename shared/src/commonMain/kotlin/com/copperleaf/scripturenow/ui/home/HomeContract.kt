package com.copperleaf.scripturenow.ui.home

object HomeContract {
    data class State(
        val loading: Boolean = false,
    )

    sealed class Inputs {
        object Initialize : Inputs()
        object GoBack : Inputs()
    }

    sealed class Events {
        object NavigateUp : Events()
    }
}
