package com.caseyjbrooks.scripturenow.appwidgets.votd

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.caseyjbrooks.scripturenow.appwidgets.launchInReceiver
import com.caseyjbrooks.scripturenow.repositories.RepositoriesInjectorProvider
import com.copperleaf.ballast.repository.cache.awaitValue

class VerseOfTheDayWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = VerseOfTheDayWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        launchInReceiver {
            (context.applicationContext as RepositoriesInjectorProvider)
                .getRepositoriesInjector()
                .getVerseOfTheDayRepository()
                .getCurrentVerseOfTheDay()
                .awaitValue()
        }
    }
}
