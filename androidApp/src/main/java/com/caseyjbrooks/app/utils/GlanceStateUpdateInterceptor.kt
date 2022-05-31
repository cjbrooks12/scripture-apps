package com.caseyjbrooks.app.utils

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.copperleaf.scripturenow.utils.OnStateUpdatedInterceptor

abstract class GlanceStateUpdateInterceptor<Inputs : Any, State : Any, WidgetClass : GlanceAppWidget>(
    protected val widgetClass: Class<WidgetClass>,
    protected val applicationContext: Context,
) : OnStateUpdatedInterceptor<Inputs, Any, State>() {

    abstract fun saveGlanceState(prefs: MutablePreferences, state: State)
    abstract fun getWidget(): WidgetClass

    override suspend fun doOnStateChanged(state: State) {
        val ids = GlanceAppWidgetManager(applicationContext).getGlanceIds(widgetClass)

        ids.forEach { glanceId ->
            updateAppWidgetState(applicationContext, PreferencesGlanceStateDefinition, glanceId) {
                it.toMutablePreferences().apply { saveGlanceState(this, state) }
            }
            getWidget().update(applicationContext, glanceId)
        }
    }
}
