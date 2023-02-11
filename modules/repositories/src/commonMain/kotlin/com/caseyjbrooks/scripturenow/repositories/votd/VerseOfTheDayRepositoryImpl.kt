package com.caseyjbrooks.scripturenow.repositories.votd

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.build
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.eventHandler
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.withViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class VerseOfTheDayRepositoryImpl(
    coroutineScope: CoroutineScope,
    inputHandler: VerseOfTheDayRepositoryInputHandler,
    configBuilder: BallastViewModelConfiguration.Builder,
) : BasicViewModel<
        VerseOfTheDayRepositoryContract.Inputs,
        VerseOfTheDayRepositoryContract.Events,
        VerseOfTheDayRepositoryContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            inputStrategy = FifoInputStrategy()
        }
        .withViewModel(
            inputHandler = inputHandler,
            initialState = VerseOfTheDayRepositoryContract.State(),
            name = "VerseOfTheDay Repository",
        )
        .build(),
    eventHandler = eventHandler { },
), VerseOfTheDayRepository {

    override fun changeVerseOfTheDayService(service: VerseOfTheDayService) {
        TODO("Not yet implemented")
    }

    override fun getCurrentVerseOfTheDay(refreshCache: Boolean): Flow<Cached<VerseOfTheDay>> {
        trySend(VerseOfTheDayRepositoryContract.Inputs.RefreshVerseOfTheDay(refreshCache))
        return observeStates()
            .map { it.verseOfTheDay }
    }
}
