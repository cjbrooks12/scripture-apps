package com.caseyjbrooks.domain.bus

import kotlinx.coroutines.flow.SharedFlow

/**
 * Normally, data flows down through the application and event flow up, but there amy be cases where the higher levels
 * of the app need to send events downstream instead.
 */
public interface EventBus {
    /**
     * A flow of all events emitted to this Bus.
     */
    public val events: SharedFlow<Any>

    /**
     * Send an Input into the Bus, to distribute to other downstream parts of the application as needed.
     */
    public suspend fun send(event: Any)
}
