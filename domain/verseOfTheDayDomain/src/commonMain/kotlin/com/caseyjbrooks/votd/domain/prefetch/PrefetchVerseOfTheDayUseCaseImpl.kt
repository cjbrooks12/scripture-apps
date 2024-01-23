package com.caseyjbrooks.votd.domain.prefetch

import com.caseyjbrooks.votd.repository.VerseOfTheDayRepository

internal class PrefetchVerseOfTheDayUseCaseImpl(
    private val verseOfTheDayRepository: VerseOfTheDayRepository,
) : PrefetchVerseOfTheDayUseCase {
    override suspend operator fun invoke() {
    }
}
