package com.caseyjbrooks.scripturenow.appwidgets.votd

import android.content.Context
import com.caseyjbrooks.scripturenow.appwidgets.ScriptureNowAppWidgetInterceptor
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.repositories.votd.VerseOfTheDayRepositoryContract
import com.copperleaf.ballast.repository.cache.Cached

public class VerseOfTheDayWidgetInterceptor(
    applicationContext: Context,
) : ScriptureNowAppWidgetInterceptor<
        VerseOfTheDayRepositoryContract.Inputs,
        VerseOfTheDayRepositoryContract.Events,
        VerseOfTheDayRepositoryContract.State,
        Cached<VerseOfTheDay>
        >(
    applicationContext = applicationContext,
    getWidget = ::VerseOfTheDayWidget,
    selectProperty = { it.verseOfTheDay },
)
