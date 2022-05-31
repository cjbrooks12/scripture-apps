package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.cache.Cached
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
    configBuilder = configBuilder
        .apply {
            this.inputHandler = inputHandler
            this.initialState = MemoryVersesRepositoryContract.State()
            this.name = "MemoryVerses Repository"
        },
), MemoryVersesRepository {

    override fun getDataList(refreshCache: Boolean): Flow<Cached<List<MemoryVerse>>> {
        trySend(MemoryVersesRepositoryContract.Inputs.Initialize)
        trySend(MemoryVersesRepositoryContract.Inputs.RefreshDataList(refreshCache))
        return observeStates()
            .map { it.dataList }
    }
}
