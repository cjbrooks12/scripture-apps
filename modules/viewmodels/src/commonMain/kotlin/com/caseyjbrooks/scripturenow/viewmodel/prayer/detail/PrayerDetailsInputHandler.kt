package com.caseyjbrooks.scripturenow.viewmodel.prayer.detail

import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import kotlinx.coroutines.flow.map

public class PrayerDetailsInputHandler(
    private val prayerRepository: PrayerRepository
) : InputHandler<
        PrayerDetailsContract.Inputs,
        PrayerDetailsContract.Events,
        PrayerDetailsContract.State> {
    override suspend fun InputHandlerScope<
            PrayerDetailsContract.Inputs,
            PrayerDetailsContract.Events,
            PrayerDetailsContract.State>.handleInput(
        input: PrayerDetailsContract.Inputs
    ): Unit = when (input) {
        is PrayerDetailsContract.Inputs.Initialize -> {
            observeFlows(
                "focused memory verse",
                prayerRepository
                    .getPrayer(input.verseUuid)
                    .map { PrayerDetailsContract.Inputs.PrayerUpdated(it) }
            )
        }

        is PrayerDetailsContract.Inputs.PrayerUpdated -> {
            updateState { it.copy(prayer = input.prayer) }
        }

        is PrayerDetailsContract.Inputs.GoBack -> {
            postEvent(PrayerDetailsContract.Events.NavigateBack)
        }

        is PrayerDetailsContract.Inputs.EditVerse -> {
            val cachedVerse = getCurrentState().prayer.getCachedOrNull()

            if (cachedVerse != null) {
//                postEvent(
//                    PrayerDetailsContract.Events.NavigateTo(
//                        Destinations.App.Verses.Edit.directions(
//                            pathParameters = mapOf(
//                                "verseId" to listOf(cachedVerse.uuid.toString())
//                            )
//                        )
//                    )
//                )
            } else {
                noOp()
            }
        }

        is PrayerDetailsContract.Inputs.DeleteVerse -> {
            val cachedVerse = getCurrentState().prayer.getCachedOrNull()

            if (cachedVerse != null) {
                prayerRepository.deletePrayer(cachedVerse)
                postEvent(PrayerDetailsContract.Events.NavigateBack)
            } else {
                noOp()
            }
        }
    }
}
