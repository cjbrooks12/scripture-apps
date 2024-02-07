package com.caseyjbrooks.verses.pillars

import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.verses.VerseDataKoinModule
import com.caseyjbrooks.verses.domain.VerseDomainKoinModule
import com.caseyjbrooks.verses.screens.ScriptureMemoryScreensKoinModule

public class ScriptureMemoryPillarKoinModule : KoinModule {
    override fun relatedModules(): List<KoinModule> = listOf(
        VerseDataKoinModule(),
        VerseDomainKoinModule(),
        ScriptureMemoryScreensKoinModule(),
    )
}
