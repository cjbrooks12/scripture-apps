package com.caseyjbrooks.domain.bus

import kotlinx.coroutines.CoroutineScope

/**
 * Normally, data flows down through the application and event flow up, but there amy be cases where the higher levels
 * of the app need to send events downstream instead.
 */
public interface EventBusService {
    public fun CoroutineScope.startSubscriptions(subscriptions: List<EventBus.Subscription>)
}
