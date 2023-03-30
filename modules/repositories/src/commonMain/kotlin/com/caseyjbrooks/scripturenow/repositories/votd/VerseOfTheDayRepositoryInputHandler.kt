package com.caseyjbrooks.scripturenow.repositories.votd

import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApi
import com.caseyjbrooks.scripturenow.db.votd.VerseOfTheDayDb
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.repositories.global.GlobalRepository
import com.caseyjbrooks.scripturenow.utils.now
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postInput
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.fetchWithCache
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

public class VerseOfTheDayRepositoryInputHandler(
    private val globalRepository: GlobalRepository,
    private val api: (VerseOfTheDayService) -> VerseOfTheDayApi,
    private val db: VerseOfTheDayDb,
) : InputHandler<
        VerseOfTheDayRepositoryContract.Inputs,
        VerseOfTheDayRepositoryContract.Events,
        VerseOfTheDayRepositoryContract.State> {
    override suspend fun InputHandlerScope<
            VerseOfTheDayRepositoryContract.Inputs,
            VerseOfTheDayRepositoryContract.Events,
            VerseOfTheDayRepositoryContract.State>.handleInput(
        input: VerseOfTheDayRepositoryContract.Inputs
    ): Unit = when (input) {
        is VerseOfTheDayRepositoryContract.Inputs.Initialize -> {
            observeFlows(
                "Flows",
                globalRepository
                    .getGlobalState()
                    .map { it.appPreferences.verseOfTheDayService }
                    .distinctUntilChanged()
                    .map { VerseOfTheDayRepositoryContract.Inputs.VerseOfTheDayServiceUpdated(it) }
            )
        }

        is VerseOfTheDayRepositoryContract.Inputs.VerseOfTheDayServiceUpdated -> {
            val previousState = getAndUpdateState {
                it.copy(
                    verseOfTheDayService = input.verseOfTheDayService,
                    verseOfTheDay = Cached.NotLoaded(),
                )
            }

            if (previousState.verseOfTheDay !is Cached.NotLoaded) {
                // if we have already loaded this verse before, load it again with the new serice, updating any existing
                // subscribers
                postInput(VerseOfTheDayRepositoryContract.Inputs.RefreshVerseOfTheDay(true))
            } else {
                // If it is still NotLoaded, assume we haven't tried loading it yet, so to save on resources, don't
                // fetch it yet.
            }
        }

        is VerseOfTheDayRepositoryContract.Inputs.VerseOfTheDayUpdated -> {
            updateState { it.copy(verseOfTheDay = input.verseOfTheDay) }
        }

        is VerseOfTheDayRepositoryContract.Inputs.RefreshVerseOfTheDay -> {
            val date: LocalDate = LocalDate.now()
            val service = getCurrentState().verseOfTheDayService

            fetchWithCache(
                input = input,
                forceRefresh = input.forceRefresh,
                getValue = { it.verseOfTheDay },
                updateState = { VerseOfTheDayRepositoryContract.Inputs.VerseOfTheDayUpdated(it) },
                doFetch = {
                    val cachedValue = db.getCachedVerseOfTheDay(service, date).firstOrNull()

                    if (cachedValue == null || input.forceRefresh) {
                        val repositoryModel = api(service).getTodaysVerseOfTheDay()
                        db.saveVerseOfTheDay(repositoryModel)
                    }

                    Unit
                },
                observe = db
                    .getCachedVerseOfTheDay(service, date)
                    .filterNotNull()
            )
        }
    }
}
