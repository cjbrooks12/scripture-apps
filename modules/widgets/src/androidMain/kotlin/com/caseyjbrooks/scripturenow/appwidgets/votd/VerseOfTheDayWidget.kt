package com.caseyjbrooks.scripturenow.appwidgets.votd

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.caseyjbrooks.scripturenow.appwidgets.ScriptureNowAppWidget
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.utils.CachedSerializer
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import kotlinx.serialization.KSerializer

@Suppress("HttpUrlsUsage")
class VerseOfTheDayWidget : ScriptureNowAppWidget<Cached<VerseOfTheDay>>() {

    override val serializer: KSerializer<Cached<VerseOfTheDay>> = CachedSerializer(VerseOfTheDay.serializer())

    @Composable
    override fun Content(savedValue: Cached<VerseOfTheDay>) {
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
            val votd = savedValue.getCachedOrNull()

            Text("Verse of the Day")
            if (votd != null) {
                Text(votd.text)
                Text(votd.reference.referenceText)
            }
        }
    }
}
