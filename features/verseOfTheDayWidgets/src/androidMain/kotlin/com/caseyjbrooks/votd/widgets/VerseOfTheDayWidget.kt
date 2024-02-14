package com.caseyjbrooks.votd.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import co.touchlab.kermit.Logger
import com.caseyjbrooks.di.GlobalKoinApplication
import com.caseyjbrooks.votd.domain.gettoday.GetTodaysVerseOfTheDayUseCase
import com.caseyjbrooks.votd.models.VerseOfTheDay
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import org.koin.core.parameter.parametersOf

public class VerseOfTheDayWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val koin = GlobalKoinApplication.koinApplication.koin
        val logger: Logger = koin.get { parametersOf("VerseOfTheDayWidget") }

        logger.d { "Providing VerseOfTheDayWidget for id '$id'" }

        provideContent {
            // fetch the VOTD content
            val getTodaysVerseOfTheDayUseCase: GetTodaysVerseOfTheDayUseCase = remember(koin) { koin.get() }
            val votd by getTodaysVerseOfTheDayUseCase().collectAsState(Cached.NotLoaded())

            Content(votd)
        }
    }

    @Composable
    private fun Content(verseOfTheDay: Cached<VerseOfTheDay>) {
        GlanceTheme {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(GlanceTheme.colors.background)
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Verse of the Day")
                if (verseOfTheDay.isLoading()) {
                    LoadingContent()
                } else {
                    val value = verseOfTheDay.getCachedOrNull()
                    if (value == null) {
                        EmptyValueContent()
                    } else {
                        NonEmptyValueContent(value)
                    }
                }
            }
        }
    }

    @Composable
    private fun LoadingContent() {
        CircularProgressIndicator()
    }

    @Composable
    private fun EmptyValueContent() {
        Text("Tap to view today's verse of the day!")
    }

    @Composable
    private fun NonEmptyValueContent(verseOfTheDay: VerseOfTheDay) {
        Text(verseOfTheDay.verse, modifier = GlanceModifier.padding(bottom = 8.dp))
        Text("~ ${verseOfTheDay.reference}")
    }
}
