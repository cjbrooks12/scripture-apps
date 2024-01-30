package com.caseyjbrooks.votd.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.caseyjbrooks.di.GlobalScriptureNowKoinApplication
import com.caseyjbrooks.votd.domain.gettoday.GetTodaysVerseOfTheDayUseCase
import com.caseyjbrooks.votd.models.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.awaitValue
import com.copperleaf.ballast.repository.cache.getValueOrNull

public class VerseOfTheDayWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val getTodaysVerseOfTheDayUseCase: GetTodaysVerseOfTheDayUseCase = GlobalScriptureNowKoinApplication
            .koinApplication.koin
            .get()

        val votd = getTodaysVerseOfTheDayUseCase().awaitValue().getValueOrNull()

        provideContent {
            Content(votd)
        }
    }

    @Composable
    private fun Content(verseOfTheDay: VerseOfTheDay?) {
        GlanceTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(GlanceTheme.colors.background)
                    .padding(8.dp),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Verse of the Day", modifier = GlanceModifier.padding(12.dp))

                if (verseOfTheDay != null) {
                    Text(verseOfTheDay.verse, modifier = GlanceModifier.padding(bottom = 8.dp))
                    Text("~ ${verseOfTheDay.reference}")
                } else {
                    Text("Tap to view")
                }
            }
        }
    }
}
