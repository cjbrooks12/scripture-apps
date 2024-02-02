package com.caseyjbrooks.votd

import com.caseyjbrooks.votd.repository.VerseOfTheDayRepository
import com.caseyjbrooks.votd.repository.VerseOfTheDayRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val realVerseOfTheDayDataModule: Module = module {
    singleOf(::VerseOfTheDayRepositoryImpl).bind<VerseOfTheDayRepository>()
}

public val fakeVerseOfTheDayDataModule: Module = module {
    singleOf(::VerseOfTheDayRepositoryImpl).bind<VerseOfTheDayRepository>()
}
