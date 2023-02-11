package com.caseyjbrooks.scripturenow.appwidgets.votd

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.caseyjbrooks.scripturenow.appwidgets.votd.VerseOfTheDayWidget.Companion.KEY_IS_ERROR
import com.caseyjbrooks.scripturenow.appwidgets.votd.VerseOfTheDayWidget.Companion.KEY_IS_LOADING
import com.caseyjbrooks.scripturenow.appwidgets.votd.VerseOfTheDayWidget.Companion.KEY_VOTD_REFERENCE
import com.caseyjbrooks.scripturenow.appwidgets.votd.VerseOfTheDayWidget.Companion.KEY_VOTD_TEXT
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepositoryContract
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.copperleaf.ballast.*
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

public class VerseOfTheDayWidgetInterceptor(
    private val applicationContext: Context,
) : BallastInterceptor<
        VerseOfTheDayRepositoryContract.Inputs,
        VerseOfTheDayRepositoryContract.Events,
        VerseOfTheDayRepositoryContract.State> {

    override fun BallastInterceptorScope<
            VerseOfTheDayRepositoryContract.Inputs,
            VerseOfTheDayRepositoryContract.Events,
            VerseOfTheDayRepositoryContract.State>.start(
        notifications: Flow<BallastNotification<
                VerseOfTheDayRepositoryContract.Inputs,
                VerseOfTheDayRepositoryContract.Events,
                VerseOfTheDayRepositoryContract.State>>
    ) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            notifications.awaitViewModelStart()
            notifications
                .states { it }
                .map { it.verseOfTheDay }
                .onEach { doOnStateChanged(it) }
                .collect()
        }
    }

    private suspend fun doOnStateChanged(verseOfTheDay: Cached<VerseOfTheDay>) {
        GlanceAppWidgetManager(applicationContext)
            .getGlanceIds(VerseOfTheDayWidget::class.java)
            .forEach { glanceId ->
                updateAppWidgetState(applicationContext, PreferencesGlanceStateDefinition, glanceId) {
                    it
                        .toMutablePreferences()
                        .apply {
                            this[booleanPreferencesKey(KEY_IS_LOADING)] = verseOfTheDay.isLoading()
                            this[booleanPreferencesKey(KEY_IS_ERROR)] = verseOfTheDay is Cached.FetchingFailed

                            this[stringPreferencesKey(KEY_VOTD_TEXT)] = verseOfTheDay
                                .getCachedOrNull()
                                ?.reference
                                ?.referenceText ?: ""
                            this[stringPreferencesKey(KEY_VOTD_REFERENCE)] = verseOfTheDay
                                .getCachedOrNull()
                                ?.text ?: ""
                        }
                }
                VerseOfTheDayWidget().update(applicationContext, glanceId)
            }
    }
}
