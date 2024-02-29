package com.caseyjbrooks.votd.widgets

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.caseyjbrooks.di.GlobalKoinApplication
import com.caseyjbrooks.domain.bus.EventBus
import com.caseyjbrooks.votd.domain.VerseOfTheDayDomainEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

public actual class VerseOfTheDayWidgetsEventBusSubscription : EventBus.Subscription {
    override fun CoroutineScope.startSubscription(bus: EventBus) {
        bus
            .events
            .filterIsInstance<VerseOfTheDayDomainEvents.VerseOfTheDayUpdated>()
            .onEach {
                val context: Context = GlobalKoinApplication.get()
                Log.d("VotdSchedules", "Receiving event: $it")
                val manager = GlanceAppWidgetManager(context)
                val widget = VerseOfTheDayWidget()
                val glanceIds = manager.getGlanceIds(widget.javaClass)
                glanceIds.forEach { glanceId ->
                    widget.update(context, glanceId)
                }
            }
            .launchIn(this)
    }
}
