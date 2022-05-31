package com.copperleaf.scripturenow.repositories.votd

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postInput
import com.copperleaf.ballast.repository.bus.EventBus
import com.copperleaf.ballast.repository.bus.observeInputsFromBus
import com.copperleaf.ballast.repository.cache.fetchWithCache
import com.copperleaf.scripturenow.api.votd.VerseOfTheDayApi
import com.copperleaf.scripturenow.common.now
import com.copperleaf.scripturenow.db.votd.VerseOfTheDayDb
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDate

class VerseOfTheDayRepositoryInputHandler(
    private val eventBus: EventBus,
    private val api: VerseOfTheDayApi,
    private val db: VerseOfTheDayDb,
) : InputHandler<
    VerseOfTheDayRepositoryContract.Inputs,
    Any,
    VerseOfTheDayRepositoryContract.State> {
    override suspend fun InputHandlerScope<
        VerseOfTheDayRepositoryContract.Inputs,
        Any,
        VerseOfTheDayRepositoryContract.State>.handleInput(
        input: VerseOfTheDayRepositoryContract.Inputs
    ) = when (input) {
        is VerseOfTheDayRepositoryContract.Inputs.ClearCaches -> {
            updateState { VerseOfTheDayRepositoryContract.State() }
        }
        is VerseOfTheDayRepositoryContract.Inputs.Initialize -> {
            val previousState = getCurrentState()

            if (!previousState.initialized) {
                updateState { it.copy(initialized = true) }
                // start observing flows here
                logger.debug("initializing")
                observeFlows(
                    key = "Observe account changes",
                    eventBus
                        .observeInputsFromBus<VerseOfTheDayRepositoryContract.Inputs>(),
                )
            } else {
                logger.debug("already initialized")
                noOp()
            }
        }
        is VerseOfTheDayRepositoryContract.Inputs.RefreshAllCaches -> {
            // then refresh all the caches in this repository
            val currentState = getCurrentState()
            if (currentState.verseOfTheDayInitialized) {
                postInput(VerseOfTheDayRepositoryContract.Inputs.RefreshVerseOfTheDay(true))
            }

            Unit
        }

        is VerseOfTheDayRepositoryContract.Inputs.DataListUpdated -> {
            updateState { it.copy(verseOfTheDay = input.verseOfTheDay) }
        }
        is VerseOfTheDayRepositoryContract.Inputs.RefreshVerseOfTheDay -> {
            updateState { it.copy(verseOfTheDayInitialized = true) }
            val date: LocalDate = LocalDate.now()

            fetchWithCache(
                input = input,
                forceRefresh = input.forceRefresh,
                getValue = { it.verseOfTheDay },
                updateState = { VerseOfTheDayRepositoryContract.Inputs.DataListUpdated(it) },
                doFetch = {
                    val cachedValue = db.getVerseOfTheDay(date).firstOrNull()

                    if (cachedValue == null || input.forceRefresh) {
                        val repositoryModel = api.getVerseOfTheDay(date)
                        db.saveVerseOfTheDay(repositoryModel)
                    }

                    Unit
                },
                observe = db
                    .getVerseOfTheDay(date)
                    .filterNotNull()
            )
        }
    }
}
