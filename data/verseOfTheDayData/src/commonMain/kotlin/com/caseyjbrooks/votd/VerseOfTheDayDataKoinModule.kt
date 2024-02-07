package com.caseyjbrooks.votd

import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.votd.repository.VerseOfTheDayRepository
import com.caseyjbrooks.votd.repository.VerseOfTheDayRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

public class VerseOfTheDayDataKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        singleOf(::VerseOfTheDayRepositoryImpl).bind<VerseOfTheDayRepository>()
    }
}
