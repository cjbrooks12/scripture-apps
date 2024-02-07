package com.caseyjbrooks.verses.screens

import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.verses.screens.detail.VerseDetailKoinModule
import com.caseyjbrooks.verses.screens.form.VerseFormKoinModule
import com.caseyjbrooks.verses.screens.list.VerseListKoinModule
import com.caseyjbrooks.verses.screens.practice.VersePracticeKoinModule

public class ScriptureMemoryScreensKoinModule : KoinModule {
    override fun relatedModules(): List<KoinModule> = listOf(
        VerseDetailKoinModule(),
        VerseFormKoinModule(),
        VerseListKoinModule(),
        VersePracticeKoinModule(),
    )
}
