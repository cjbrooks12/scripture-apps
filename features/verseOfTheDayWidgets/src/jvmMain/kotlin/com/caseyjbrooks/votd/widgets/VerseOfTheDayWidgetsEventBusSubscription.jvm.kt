package com.caseyjbrooks.votd.widgets

import com.caseyjbrooks.domain.bus.EventBus
import kotlinx.coroutines.CoroutineScope

public actual class VerseOfTheDayWidgetsEventBusSubscription : EventBus.Subscription {
    override fun CoroutineScope.startSubscription(bus: EventBus) {
    }
}
