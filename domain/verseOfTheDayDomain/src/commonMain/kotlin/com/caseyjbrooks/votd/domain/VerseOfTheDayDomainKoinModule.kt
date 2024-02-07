package com.caseyjbrooks.votd.domain

import com.caseyjbrooks.di.KoinModule
import com.caseyjbrooks.votd.domain.gettoday.GetTodaysVerseOfTheDayUseCase
import com.caseyjbrooks.votd.domain.gettoday.GetTodaysVerseOfTheDayUseCaseImpl
import com.caseyjbrooks.votd.domain.prefetch.PrefetchVerseOfTheDayUseCase
import com.caseyjbrooks.votd.domain.prefetch.PrefetchVerseOfTheDayUseCaseImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public class VerseOfTheDayDomainKoinModule : KoinModule {
    override fun mainModule(): Module = module {
        factoryOf(::GetTodaysVerseOfTheDayUseCaseImpl).bind<GetTodaysVerseOfTheDayUseCase>()
        factoryOf(::PrefetchVerseOfTheDayUseCaseImpl).bind<PrefetchVerseOfTheDayUseCase>()
    }
}
