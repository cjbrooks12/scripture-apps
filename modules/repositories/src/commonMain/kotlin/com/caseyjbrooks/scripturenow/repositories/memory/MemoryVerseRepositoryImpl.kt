package com.caseyjbrooks.scripturenow.repositories.memory

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.VerseReference
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDay
import com.caseyjbrooks.scripturenow.repositories.FormLoader
import com.caseyjbrooks.scripturenow.utils.referenceText
import com.copperleaf.ballast.*
import com.copperleaf.ballast.core.BasicViewModel
import com.copperleaf.ballast.core.BootstrapInterceptor
import com.copperleaf.ballast.core.FifoInputStrategy
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.map
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class MemoryVerseRepositoryImpl(
    coroutineScope: CoroutineScope,
    configBuilder: BallastViewModelConfiguration.Builder,
    inputHandler: MemoryVerseRepositoryInputHandler,
    private val memoryVerseFormLoader: FormLoader,
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

    override fun getMainVerse(refreshCache: Boolean): Flow<Cached<MemoryVerse>> {
        trySend(MemoryVerseRepositoryContract.Inputs.RefreshMemoryVerseList(refreshCache))
        return observeStates()
            .map { it.mainMemoryVerse }
    }

    override fun getAllVerses(refreshCache: Boolean): Flow<Cached<List<MemoryVerse>>> {
        trySend(MemoryVerseRepositoryContract.Inputs.RefreshMemoryVerseList(refreshCache))
        return observeStates()
            .map { it.memoryVerseList }
    }

    override fun getVerseById(uuid: Uuid, refreshCache: Boolean): Flow<Cached<MemoryVerse>> {
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

    override fun getVerseByReference(reference: VerseReference, refreshCache: Boolean): Flow<Cached<MemoryVerse>> {
        trySend(MemoryVerseRepositoryContract.Inputs.RefreshMemoryVerseList(refreshCache))
        return observeStates()
            .map {
                it
                    .memoryVerseList
                    .map { memoryVerses ->
                        memoryVerses.single { verse -> verse.reference.referenceText == reference.referenceText }
                    }
            }
    }

    override fun loadForm(): Flow<Cached<Pair<JsonSchema, UiSchema>>> {
        return memoryVerseFormLoader.loadForm()
    }

    override suspend fun createOrUpdateVerse(verse: MemoryVerse) {
        send(MemoryVerseRepositoryContract.Inputs.CreateOrUpdateMemoryVerse(verse))
    }

    override suspend fun saveAsMemoryVerse(verse: VerseOfTheDay) {
        send(MemoryVerseRepositoryContract.Inputs.SaveAsMemoryVerse(verse))
    }

    override suspend fun setAsMainMemoryVerse(verse: MemoryVerse) {
        send(MemoryVerseRepositoryContract.Inputs.SetAsMainVerse(verse))
    }

    override suspend fun clearMainMemoryVerse() {
        send(MemoryVerseRepositoryContract.Inputs.ClearMainVerse)
    }

    override suspend fun deleteVerse(verse: MemoryVerse) {
        send(MemoryVerseRepositoryContract.Inputs.DeleteMemoryVerse(verse))
    }
}
