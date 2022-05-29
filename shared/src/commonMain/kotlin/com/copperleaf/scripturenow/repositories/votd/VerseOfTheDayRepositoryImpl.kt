package com.copperleaf.scripturenow.repositories.votd

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.scripturenow.repositories.votd.models.VerseOfTheDay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VerseOfTheDayRepositoryImpl(
    coroutineScope: CoroutineScope,
    eventBus: EventBus,
    inputHandler: VerseOfTheDayRepositoryInputHandler,
    configBuilder: BallastViewModelConfiguration.Builder,
) : BallastRepository<
    VerseOfTheDayRepositoryContract.Inputs,
    VerseOfTheDayRepositoryContract.State>(
    coroutineScope = coroutineScope,
    eventBus = eventBus,
    configBuilder = configBuilder
        .apply {
            this.inputHandler = inputHandler
            this.initialState = VerseOfTheDayRepositoryContract.State()
            this.name = "VerseOfTheDay Repository"
        },
), VerseOfTheDayRepository {
    override fun clearAllCaches() {
        trySend(VerseOfTheDayRepositoryContract.Inputs.ClearCaches)
    }

    override fun getVerseOfTheDay(refreshCache: Boolean): Flow<Cached<VerseOfTheDay>> {
        trySend(VerseOfTheDayRepositoryContract.Inputs.Initialize)
        trySend(VerseOfTheDayRepositoryContract.Inputs.RefreshVerseOfTheDay(refreshCache))
        return observeStates()
            .map { it.verseOfTheDay }
    }
}
