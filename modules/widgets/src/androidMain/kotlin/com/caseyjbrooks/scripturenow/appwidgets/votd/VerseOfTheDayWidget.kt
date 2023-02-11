package com.caseyjbrooks.scripturenow.appwidgets.votd

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions

@Suppress("HttpUrlsUsage")
class VerseOfTheDayWidget : GlanceAppWidget() {

    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(
            DpSize(40.dp, 40.dp), // 1x1
            DpSize(100.dp, 40.dp), // 2x1 or greater
            DpSize(80.dp, 80.dp), // 2x2 or greater
        )
    )

    @Composable
    override fun Content() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .cornerRadius(4.dp)
                .background(Color.White)
                .clickable(
                    actionStartActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "scripture://now${ScriptureNowRoute.VerseOfTheDay.directions().build()}".toUri()
                        )
                    )
                )
                .padding(8.dp)
        ) {
            val prefs = currentState<Preferences>()

            Text("Verse of the Day")
            Text(prefs[stringPreferencesKey(KEY_VOTD_TEXT)] ?: "")
            Text(prefs[stringPreferencesKey(KEY_VOTD_REFERENCE)] ?: "")
        }
    }

    companion object {
        internal const val KEY_IS_LOADING = "votd_is_loading"
        internal const val KEY_IS_ERROR = "votd_is_error"
        internal const val KEY_VOTD_TEXT = "votd_text"
        internal const val KEY_VOTD_REFERENCE = "votd_reference"
    }
}
