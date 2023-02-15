package com.caseyjbrooks.scripturenow.appwidgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.currentState
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

@Suppress("HttpUrlsUsage")
abstract class ScriptureNowAppWidget<T : Any> : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            DpSize(40.dp, 40.dp), // 1x1
            DpSize(100.dp, 40.dp), // 2x1 or greater
            DpSize(80.dp, 80.dp), // 2x2 or greater
        )
    )

    abstract val serializer: KSerializer<T>

    @Composable
    final override fun Content() {
        val prefs = currentState<Preferences>()
        val widgetValue = runCatching {
            Json.decodeFromString(serializer, prefs[WIDGET_JSON]!!)
        }.getOrNull()
        if (widgetValue != null) {
            Content(widgetValue)
        }
    }

    @Composable
    abstract fun Content(savedValue: T)

    companion object {
        internal val WIDGET_JSON = stringPreferencesKey("scripture_now_widget_json")
    }
}
