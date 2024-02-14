package com.caseyjbrooks.domain.bus

import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

public class EventBusImpl(
    private val logger: Logger
) : EventBus {
    private val _events = MutableSharedFlow<Any>()
    override val events: SharedFlow<Any> get() = _events.asSharedFlow()

    override suspend fun send(event: Any) {
        logger.d { "Emitting event: $event" }
        _events.emit(event)
    }
}
