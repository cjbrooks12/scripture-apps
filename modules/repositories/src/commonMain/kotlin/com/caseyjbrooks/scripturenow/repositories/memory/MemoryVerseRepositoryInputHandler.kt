package com.caseyjbrooks.scripturenow.repositories.memory

import com.caseyjbrooks.scripturenow.db.memory.MemoryVerseDb
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayToMemoryVerseConverter
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import com.copperleaf.ballast.repository.cache.fetchWithCache

public class MemoryVerseRepositoryInputHandler(
    private val db: MemoryVerseDb,
    private val verseOfTheDayToMemoryVerseConverter: VerseOfTheDayToMemoryVerseConverter,
) : InputHandler<
        MemoryVerseRepositoryContract.Inputs,
        MemoryVerseRepositoryContract.Events,
        MemoryVerseRepositoryContract.State> {
    override suspend fun InputHandlerScope<
            MemoryVerseRepositoryContract.Inputs,
            MemoryVerseRepositoryContract.Events,
            MemoryVerseRepositoryContract.State>.handleInput(
        input: MemoryVerseRepositoryContract.Inputs
    ): Unit = when (input) {
        is MemoryVerseRepositoryContract.Inputs.ClearCaches -> {
            updateState { MemoryVerseRepositoryContract.State() }
        }

        is MemoryVerseRepositoryContract.Inputs.Initialize -> {
            val previousState = getCurrentState()

            if (!previousState.initialized) {
                updateState { it.copy(initialized = true) }
                // start observing flows here
                logger.debug("initializing")
//                observeFlows(
//                    key = "Observe account changes",
//                    eventBus
//                        .observeInputsFromBus<MemoryVersesRepositoryContract.Inputs>(),
//                )
            } else {
                logger.debug("already initialized")
                noOp()
            }
        }

        is MemoryVerseRepositoryContract.Inputs.RefreshAllCaches -> {
            // then refresh all the caches in this repository
            val currentState = getCurrentState()
            if (currentState.memoryVerseListInitialized) {
                postInput(MemoryVerseRepositoryContract.Inputs.RefreshMemoryVerseList(true))
            }

            Unit
        }

        is MemoryVerseRepositoryContract.Inputs.MemoryVerseListUpdated -> {
            updateState { it.copy(memoryVerseList = input.memoryVerseList) }
        }

        is MemoryVerseRepositoryContract.Inputs.RefreshMemoryVerseList -> {
            updateState { it.copy(memoryVerseListInitialized = true) }
            fetchWithCache(
                input = input,
                forceRefresh = input.forceRefresh,
                getValue = { it.memoryVerseList },
                updateState = { MemoryVerseRepositoryContract.Inputs.MemoryVerseListUpdated(it) },
                doFetch = { },
                observe = db.getMemoryVerses()
            )
        }

        is MemoryVerseRepositoryContract.Inputs.CreateOrUpdateMemoryVerse -> {
            sideJob(input.toString()) {
                db.saveVerse(input.memoryVerse)
            }
        }

        is MemoryVerseRepositoryContract.Inputs.SaveAsMemoryVerse -> {
            sideJob(input.toString()) {
                db.saveVerse(
                    verseOfTheDayToMemoryVerseConverter.convertVerseOfTheDayToMemoryVerse(input.verseOfTheDay)
                )
            }
        }

        is MemoryVerseRepositoryContract.Inputs.SetAsMainVerse -> {
            sideJob(input.toString()) {
                db.setAsMainVerse(input.memoryVerse)
            }
        }

        is MemoryVerseRepositoryContract.Inputs.ClearMainVerse -> {
            sideJob(input.toString()) {
                db.clearMainVerse()
            }
        }

        is MemoryVerseRepositoryContract.Inputs.DeleteMemoryVerse -> {
            sideJob(input.toString()) {
                db.deleteVerse(input.memoryVerse)
            }
        }
    }
}
