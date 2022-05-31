package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.cache.Cached
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MemoryVersesRepositoryImpl(
    coroutineScope: CoroutineScope,
    eventBus: EventBus,
    configBuilder: BallastViewModelConfiguration.Builder,
) : BallastRepository<
    MemoryVersesRepositoryContract.Inputs,
    MemoryVersesRepositoryContract.State>(
    coroutineScope = coroutineScope,
    eventBus = eventBus,
    configBuilder = configBuilder
        .apply {
            this.inputHandler = MemoryVersesRepositoryInputHandler(eventBus)
            this.initialState = MemoryVersesRepositoryContract.State()
            this.name = "MemoryVerses Repository"
        },
), MemoryVersesRepository {
    override fun clearAllCaches() {
        trySend(MemoryVersesRepositoryContract.Inputs.ClearCaches)
    }

    override fun getDataList(refreshCache: Boolean): Flow<Cached<List<String>>> {
        trySend(MemoryVersesRepositoryContract.Inputs.Initialize)
        trySend(MemoryVersesRepositoryContract.Inputs.RefreshDataList(refreshCache))
        return observeStates()
            .map { it.dataList }
    }
}
