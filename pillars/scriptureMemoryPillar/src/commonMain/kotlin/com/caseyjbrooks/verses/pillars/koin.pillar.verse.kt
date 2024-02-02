package com.caseyjbrooks.verses.pillars

import com.caseyjbrooks.verses.domain.verseDomainModule
import com.caseyjbrooks.verses.realVerseDataModule
import com.caseyjbrooks.verses.screens.verseScreensModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val versePillarModule: Module = module {
    includes(
        realVerseDataModule,
        verseDomainModule,
        verseScreensModule,
    )
}
