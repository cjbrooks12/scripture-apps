package com.copperleaf.scripturenow.repositories.verses

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postInput
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.bus.observeInputsFromBus
import com.copperleaf.ballast.repository.cache.fetchWithCache

class MemoryVersesRepositoryInputHandler(
    private val eventBus: EventBus,
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
            if (currentState.dataListInitialized) {
                postInput(MemoryVersesRepositoryContract.Inputs.RefreshDataList(true))
            }

            Unit
        }

        is MemoryVersesRepositoryContract.Inputs.DataListUpdated -> {
            updateState { it.copy(dataList = input.dataList) }
        }
        is MemoryVersesRepositoryContract.Inputs.RefreshDataList -> {
            updateState { it.copy(dataListInitialized = true) }
            fetchWithCache(
                input = input,
                forceRefresh = input.forceRefresh,
                getValue = { it.dataList },
                updateState = { MemoryVersesRepositoryContract.Inputs.DataListUpdated(it) },
                doFetch = { TODO() },
            )
        }
    }
}
