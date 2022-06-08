package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postInput
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.bus.observeInputsFromBus
import com.copperleaf.ballast.repository.cache.fetchWithCache
import com.copperleaf.scripturenow.db.verses.MemoryVersesDb

class MemoryVersesRepositoryInputHandler(
    private val eventBus: EventBus,
    private val db: MemoryVersesDb,
) : InputHandler<
    MemoryVersesRepositoryContract.Inputs,
    Any,
    MemoryVersesRepositoryContract.State> {
    override suspend fun InputHandlerScope<
        MemoryVersesRepositoryContract.Inputs,
        Any,
        MemoryVersesRepositoryContract.State>.handleInput(
        input: MemoryVersesRepositoryContract.Inputs
    ) = when (input) {
        is MemoryVersesRepositoryContract.Inputs.ClearCaches -> {
            updateState { MemoryVersesRepositoryContract.State() }
        }
        is MemoryVersesRepositoryContract.Inputs.Initialize -> {
            val previousState = getCurrentState()

            if (!previousState.initialized) {
                updateState { it.copy(initialized = true) }
                // start observing flows here
                logger.debug("initializing")
                observeFlows(
                    key = "Observe account changes",
                    eventBus
                        .observeInputsFromBus<MemoryVersesRepositoryContract.Inputs>(),
                )
            } else {
                logger.debug("already initialized")
                noOp()
            }
        }
        is MemoryVersesRepositoryContract.Inputs.RefreshAllCaches -> {
            // then refresh all the caches in this repository
            val currentState = getCurrentState()
            if (currentState.memoryVerseListInitialized) {
                postInput(MemoryVersesRepositoryContract.Inputs.RefreshMemoryVerseList(true))
            }

            Unit
        }

        is MemoryVersesRepositoryContract.Inputs.MemoryVerseListUpdated -> {
            updateState { it.copy(memoryVerseList = input.memoryVerseList) }
        }
        is MemoryVersesRepositoryContract.Inputs.RefreshMemoryVerseList -> {
            updateState { it.copy(memoryVerseListInitialized = true) }
            fetchWithCache(
                input = input,
                forceRefresh = input.forceRefresh,
                getValue = { it.memoryVerseList },
                updateState = { MemoryVersesRepositoryContract.Inputs.MemoryVerseListUpdated(it) },
                doFetch = { },
                observe = db.getMemoryVerses()
            )
        }
        is MemoryVersesRepositoryContract.Inputs.CreateOrUpdateMemoryVerse -> {
            sideJob(input.toString()) {
                db.saveVerse(input.memoryVerse)
            }
        }
        is MemoryVersesRepositoryContract.Inputs.DeleteMemoryVerse -> {
            sideJob(input.toString()) {
                db.deleteVerse(input.memoryVerse)
            }
        }
    }
}
