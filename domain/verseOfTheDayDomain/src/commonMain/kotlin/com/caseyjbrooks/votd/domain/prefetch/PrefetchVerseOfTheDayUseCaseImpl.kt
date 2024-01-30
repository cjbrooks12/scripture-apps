package com.caseyjbrooks.votd.domain.prefetch

import com.caseyjbrooks.votd.repository.VerseOfTheDayRepository

internal class PrefetchVerseOfTheDayUseCaseImpl(
    private val repository: VerseOfTheDayRepository,
) : PrefetchVerseOfTheDayUseCase {
    override suspend operator fun invoke() {
        repository.fetchAndCacheVerseOfTheDay()
    }
}
