package com.caseyjbrooks.scripturenow.repositories.prayer

import com.caseyjbrooks.scripturenow.db.prayer.PrayerDb
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.postInput
import com.copperleaf.ballast.repository.cache.fetchWithCache

public class PrayerRepositoryInputHandler(
    private val db: PrayerDb,
) : InputHandler<
        PrayerRepositoryContract.Inputs,
        PrayerRepositoryContract.Events,
        PrayerRepositoryContract.State> {
    override suspend fun InputHandlerScope<
            PrayerRepositoryContract.Inputs,
            PrayerRepositoryContract.Events,
            PrayerRepositoryContract.State>.handleInput(
        input: PrayerRepositoryContract.Inputs
    ): Unit = when (input) {
        is PrayerRepositoryContract.Inputs.ClearCaches -> {
            updateState { PrayerRepositoryContract.State() }
        }

        is PrayerRepositoryContract.Inputs.Initialize -> {
            val previousState = getCurrentState()

            if (!previousState.initialized) {
                updateState { it.copy(initialized = true) }
                // start observing flows here
                logger.debug("initializing")
            } else {
                logger.debug("already initialized")
                noOp()
            }
        }

        is PrayerRepositoryContract.Inputs.RefreshAllCaches -> {
            // then refresh all the caches in this repository
            val currentState = getCurrentState()
            if (currentState.prayerListInitialized) {
                postInput(PrayerRepositoryContract.Inputs.RefreshPrayerList(true))
            }

            Unit
        }

        is PrayerRepositoryContract.Inputs.PrayerListUpdated -> {
            updateState { it.copy(prayerList = input.prayerList) }
        }

        is PrayerRepositoryContract.Inputs.RefreshPrayerList -> {
            updateState { it.copy(prayerListInitialized = true) }
            fetchWithCache(
                input = input,
                forceRefresh = input.forceRefresh,
                getValue = { it.prayerList },
                updateState = { PrayerRepositoryContract.Inputs.PrayerListUpdated(it) },
                doFetch = { },
                observe = db.getPrayers()
            )
        }

        is PrayerRepositoryContract.Inputs.CreateOrUpdatePrayer -> {
            sideJob(input.toString()) {
                db.savePrayer(input.prayer)
            }
        }

        is PrayerRepositoryContract.Inputs.DeletePrayer -> {
            sideJob(input.toString()) {
                db.deletePrayer(input.prayer)
            }
        }
    }
}
