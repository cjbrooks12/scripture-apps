package com.caseyjbrooks.votd.pillars

import com.caseyjbrooks.domain.bus.EventBus
import com.caseyjbrooks.routing.Pillar
import com.caseyjbrooks.votd.widgets.VerseOfTheDayWidgetsEventBusSubscription

public object VotdPillar : Pillar {
    override val eventBusSubscriptions: List<EventBus.Subscription> = listOf(
        VerseOfTheDayWidgetsEventBusSubscription()
    )
}
