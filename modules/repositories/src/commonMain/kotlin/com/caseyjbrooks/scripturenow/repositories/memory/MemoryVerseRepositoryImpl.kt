package com.caseyjbrooks.scripturenow.repositories.memory

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.copperleaf.ballast.*
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class MemoryVerseRepositoryImpl(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: MemoryVerseRepositoryInputHandler,
) : BasicViewModel<
        MemoryVerseRepositoryContract.Inputs,
        MemoryVerseRepositoryContract.Events,
        MemoryVerseRepositoryContract.State>(
    coroutineScope = coroutineScope,
    config = configBuilder
        .apply {
            this += BootstrapInterceptor {
                MemoryVerseRepositoryContract.Inputs.Initialize
            }
            inputStrategy = FifoInputStrategy()
        }
        .withViewModel(
            inputHandler = inputHandler,
            initialState = MemoryVerseRepositoryContract.State(),
            name = "MemoryVerses Repository",
        )
        .build(),
    eventHandler = eventHandler { },
), MemoryVerseRepository {

    override fun getAllVerses(refreshCache: Boolean): Flow<Cached<List<MemoryVerse>>> {
        trySend(MemoryVerseRepositoryContract.Inputs.RefreshMemoryVerseList(refreshCache))
        return observeStates()
            .map { it.memoryVerseList }
    }

    override fun getVerse(uuid: Uuid, refreshCache: Boolean): Flow<Cached<MemoryVerse>> {
        trySend(MemoryVerseRepositoryContract.Inputs.RefreshMemoryVerseList(refreshCache))
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
        send(MemoryVerseRepositoryContract.Inputs.CreateOrUpdateMemoryVerse(verse))
    }

    override suspend fun saveAsMemoryVerse(verse: VerseOfTheDay) {
        send(MemoryVerseRepositoryContract.Inputs.SaveAsMemoryVerse(verse))
    }

    override suspend fun deleteVerse(verse: MemoryVerse) {
        send(MemoryVerseRepositoryContract.Inputs.DeleteMemoryVerse(verse))
    }
}
