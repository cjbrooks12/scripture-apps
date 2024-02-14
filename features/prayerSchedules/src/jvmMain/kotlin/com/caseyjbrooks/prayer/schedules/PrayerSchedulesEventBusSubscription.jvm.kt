package com.caseyjbrooks.prayer.schedules

import com.caseyjbrooks.domain.bus.EventBus
import kotlinx.coroutines.CoroutineScope

public actual class PrayerSchedulesEventBusSubscription : EventBus.Subscription {
    override fun CoroutineScope.startSubscription(bus: EventBus) {
    }
}
