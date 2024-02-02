package com.caseyjbrooks.verses.screens

import com.caseyjbrooks.verses.screens.detail.verseDetailScreenModule
import com.caseyjbrooks.verses.screens.form.verseFormScreenModule
import com.caseyjbrooks.verses.screens.list.verseListScreenModule
import com.caseyjbrooks.verses.screens.practice.versePracticeScreenModule
import org.koin.core.module.Module
import org.koin.dsl.module

public val verseScreensModule: Module = module {
    includes(
        verseDetailScreenModule,
        verseFormScreenModule,
        verseListScreenModule,
        versePracticeScreenModule,
    )
}
