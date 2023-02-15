package com.caseyjbrooks.scripturenow.appwidgets.memory

import android.content.Context
import com.caseyjbrooks.scripturenow.appwidgets.ScriptureNowAppWidgetInterceptor
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepositoryContract
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map

public class MemoryVerseWidgetInterceptor(
    applicationContext: Context,
) : ScriptureNowAppWidgetInterceptor<
        MemoryVerseRepositoryContract.Inputs,
        MemoryVerseRepositoryContract.Events,
        MemoryVerseRepositoryContract.State,
        Cached<MemoryVerse>
        >(
    applicationContext = applicationContext,
    getWidget = ::MemoryVerseWidget,
    selectProperty = {
        it
            .memoryVerseList
            .map { verses -> verses.single { it.main } }
    },
)
