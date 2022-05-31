package com.copperleaf.scripturenow.utils

import com.copperleaf.ballast.BallastInterceptor
import com.copperleaf.ballast.BallastInterceptorScope
import com.copperleaf.ballast.BallastLogger
import com.copperleaf.ballast.BallastNotification
import com.copperleaf.ballast.Queued
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import co.touchlab.kermit.Logger as KermitLogger
import io.ktor.client.plugins.logging.Logger as KtorLogger

public class BootstrapInterceptor<Inputs : Any, Events : Any, State : Any>(
    private val getInitialInput: suspend () -> Inputs,
) : BallastInterceptor<Inputs, Events, State> {

    override fun BallastInterceptorScope<Inputs, Events, State>.start(
        notifications: Flow<BallastNotification<Inputs, Events, State>>
    ) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            // wait for the BallastNotification.ViewModelStarted to be sent
            notifications
                .filterIsInstance<BallastNotification.ViewModelStarted<Inputs, Events, State>>()
                .first()

            // generate an Input
            val initialInput = getInitialInput()

            // post the Input back to the VM
            sendToQueue(
                Queued.HandleInput(null, initialInput)
            )
        }
    }
}

abstract class OnStateUpdatedInterceptor<Inputs : Any, Events : Any, State : Any>(
) : BallastInterceptor<Inputs, Events, State> {

    abstract suspend fun doOnStateChanged(state: State)

    override suspend fun onNotify(logger: BallastLogger, notification: BallastNotification<Inputs, Events, State>) {
        if (notification is BallastNotification.StateChanged<Inputs, Events, State>) {
            doOnStateChanged(notification.state)
        }
    }
}

class KermitKtorLogger(val kermitLogger: KermitLogger) : KtorLogger {
    override fun log(message: String) {
        kermitLogger.d { message }
    }
}

class KermitBallastLogger(val kermitLogger: KermitLogger) : BallastLogger {
    override fun debug(message: String) {
        kermitLogger.d { message }
    }

    override fun error(throwable: Throwable) {
        kermitLogger.e(throwable) { throwable.message ?: "" }
    }

    override fun info(message: String) {
        kermitLogger.i { message }
    }
}

inline fun <T, U> Flow<List<T>>.mapEach(crossinline fn: (T) -> U): Flow<List<U>> {
    return this
        .map { list ->
            list.map(fn)
        }
}

inline fun <T, U> Flow<T?>.mapIfNotNull(crossinline fn: (T) -> U): Flow<U?> {
    return this
        .map { value ->
            if (value != null) fn(value) else null
        }
}
