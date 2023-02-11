package com.caseyjbrooks.scripturenow.repositories.votd

import com.caseyjbrooks.scripturenow.api.votd.VerseOfTheDayApi
import com.caseyjbrooks.scripturenow.db.votd.VerseOfTheDayDb
import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.utils.now
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.repository.cache.fetchWithCache
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.LocalDate

public class VerseOfTheDayRepositoryInputHandler(
    private val api: VerseOfTheDayApi,
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
        is VerseOfTheDayRepositoryContract.Inputs.VerseOfTheDayUpdated -> {
            updateState { it.copy(verseOfTheDay = input.verseOfTheDay) }
        }

        is VerseOfTheDayRepositoryContract.Inputs.RefreshVerseOfTheDay -> {
            val date: LocalDate = LocalDate.now()

            fetchWithCache(
                input = input,
                forceRefresh = input.forceRefresh,
                getValue = { it.verseOfTheDay },
                updateState = { VerseOfTheDayRepositoryContract.Inputs.VerseOfTheDayUpdated(it) },
                doFetch = {
                    val cachedValue = db.getCachedVerseOfTheDay(VerseOfTheDayService.OurManna, date).firstOrNull()

                    if (cachedValue == null || input.forceRefresh) {
                        val repositoryModel = api.getTodaysVerseOfTheDay()
                        db.saveVerseOfTheDay(repositoryModel)
                    }

                    Unit
                },
                observe = db
                    .getCachedVerseOfTheDay(VerseOfTheDayService.OurManna, date)
                    .filterNotNull()
            )
        }
    }
}
