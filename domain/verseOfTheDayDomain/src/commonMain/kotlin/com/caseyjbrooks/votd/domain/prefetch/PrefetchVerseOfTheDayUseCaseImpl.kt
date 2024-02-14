package com.caseyjbrooks.votd.domain.prefetch

import com.caseyjbrooks.domain.bus.EventBus
import com.caseyjbrooks.votd.domain.VerseOfTheDayDomainEvents
import com.caseyjbrooks.votd.repository.VerseOfTheDayRepository

internal class PrefetchVerseOfTheDayUseCaseImpl(
    private val repository: VerseOfTheDayRepository,
    private val eventBus: EventBus,
) : PrefetchVerseOfTheDayUseCase {
    override suspend operator fun invoke() {
        repository.fetchAndCacheVerseOfTheDay()
        eventBus.send(VerseOfTheDayDomainEvents.VerseOfTheDayUpdated)
    }
}
