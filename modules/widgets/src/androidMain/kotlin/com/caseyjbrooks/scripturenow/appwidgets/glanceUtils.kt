package com.caseyjbrooks.scripturenow.appwidgets

import android.content.BroadcastReceiver
import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun GlanceAppWidget.updateAllAppWidgetStates(
    context: Context,
    updateState: suspend MutablePreferences.() -> Unit,
) {
    val manager = GlanceAppWidgetManager(context)
    manager.getGlanceIds(javaClass).forEach { glanceId ->
        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) {
            it
                .toMutablePreferences()
                .apply { updateState() }
                .toPreferences()
        }
        update(context, glanceId)
    }
}

fun BroadcastReceiver.launchInReceiver(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult: BroadcastReceiver.PendingResult? = goAsync()
    CoroutineScope(SupervisorJob()).launch(context) {
        try {
            block()
        } finally {
            pendingResult?.finish()
        }
    }
}
