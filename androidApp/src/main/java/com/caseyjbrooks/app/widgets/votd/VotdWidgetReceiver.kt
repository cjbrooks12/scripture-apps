package com.caseyjbrooks.app.widgets.votd

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.caseyjbrooks.app.utils.GlanceStateUpdateInterceptor
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.scripturenow.repositories.votd.VerseOfTheDayRepositoryContract

class VotdWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = Widget()

    class Widget : GlanceAppWidget() {

        @Composable
        override fun Content() {
            val prefs = currentState<Preferences>()

            Column(
                modifier = GlanceModifier
                    .cornerRadius(4.dp)
                    .background(Color.White)
                    .padding(8.dp)
            ) {
                Text(prefs[stringPreferencesKey("votd_text")] ?: "")
                Text(prefs[stringPreferencesKey("votd_reference")] ?: "")
            }
        }
    }

    class Updater(applicationContext: Context) : GlanceStateUpdateInterceptor<
        VerseOfTheDayRepositoryContract.Inputs,
        VerseOfTheDayRepositoryContract.State,
        Widget,
        >(Widget::class.java, applicationContext) {
        override fun getWidget(): Widget = Widget()

        override fun saveGlanceState(prefs: MutablePreferences, state: VerseOfTheDayRepositoryContract.State) {
            val value = state.verseOfTheDay.getCachedOrNull()
            prefs[stringPreferencesKey("votd_reference")] = value?.reference ?: ""
            prefs[stringPreferencesKey("votd_text")] = value?.text ?: ""
        }
    }
}
