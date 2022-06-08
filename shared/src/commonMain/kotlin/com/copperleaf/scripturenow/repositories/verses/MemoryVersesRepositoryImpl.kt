package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.withRepository
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemoryVersesRepositoryImpl(
    coroutineScope: CoroutineScope,
    eventBus: EventBus,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: MemoryVersesRepositoryInputHandler,
) : BallastRepository<
    MemoryVersesRepositoryContract.Inputs,
    MemoryVersesRepositoryContract.State>(
    coroutineScope = coroutineScope,
    eventBus = eventBus,
    config = configBuilder
        .withRepository()
        .forViewModel(
            inputHandler = inputHandler,
            initialState = MemoryVersesRepositoryContract.State(),
            name = "MemoryVerses Repository",
        ),
), MemoryVersesRepository {

    override fun getDataList(refreshCache: Boolean): Flow<Cached<List<MemoryVerse>>> {
        trySend(MemoryVersesRepositoryContract.Inputs.Initialize)
        trySend(MemoryVersesRepositoryContract.Inputs.RefreshDataList(refreshCache))
        return observeStates()
            .map { it.dataList }
    }
}
