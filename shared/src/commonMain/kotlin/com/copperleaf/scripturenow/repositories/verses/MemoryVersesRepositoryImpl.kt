package com.copperleaf.scripturenow.repositories.verses

import com.benasher44.uuid.Uuid
import com.copperleaf.ballast.BallastViewModelConfiguration
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.forViewModel
import com.copperleaf.ballast.plusAssign
import com.copperleaf.ballast.repository.BallastRepository
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map
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
        .apply {
            this += BootstrapInterceptor {
                MemoryVersesRepositoryContract.Inputs.Initialize
            }
        }
        .forViewModel(
            inputHandler = inputHandler,
            initialState = MemoryVersesRepositoryContract.State(),
            name = "MemoryVerses Repository",
        ),
), MemoryVersesRepository {

    override fun getAllVerses(refreshCache: Boolean): Flow<Cached<List<MemoryVerse>>> {
        trySend(MemoryVersesRepositoryContract.Inputs.RefreshMemoryVerseList(refreshCache))
        return observeStates()
            .map { it.memoryVerseList }
    }

    override fun getVerse(uuid: Uuid, refreshCache: Boolean): Flow<Cached<MemoryVerse>> {
        trySend(MemoryVersesRepositoryContract.Inputs.RefreshMemoryVerseList(refreshCache))
        return observeStates()
            .map {
                it
                    .memoryVerseList
                    .map { memoryVerses ->
                        memoryVerses.single { verse -> verse.uuid == uuid }
                    }
            }
    }

    override suspend fun createOrUpdateVerse(verse: MemoryVerse) {
        send(MemoryVersesRepositoryContract.Inputs.CreateOrUpdateMemoryVerse(verse))
    }

    override suspend fun deleteVerse(verse: MemoryVerse) {
        send(MemoryVersesRepositoryContract.Inputs.DeleteMemoryVerse(verse))
    }
}
