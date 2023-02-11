package com.caseyjbrooks.scripturenow.utils

import com.copperleaf.ballast.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

public abstract class OnStateUpdatedInterceptor<
        Inputs : Any,
        Events : Any,
        State : Any> : BallastInterceptor<Inputs, Events, State> {

    protected abstract suspend fun doOnStateChanged(state: State)

    protected open fun bufferStates(bufferStates: Flow<State>): Flow<State> {
        return bufferStates
    }

    final override fun BallastInterceptorScope<Inputs, Events, State>.start(
        notifications: Flow<BallastNotification<Inputs, Events, State>>,
    ) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            notifications.awaitViewModelStart()
            notifications
                .states(::bufferStates)
                .onEach { doOnStateChanged(it) }
                .collect()
        }
    }
}

private fun dontFollowFileName() {
}
