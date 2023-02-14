package com.caseyjbrooks.scripturenow.viewmodel.prayer.detail

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
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
                    .getPrayerById(input.prayerUuid)
                    .map { PrayerDetailsContract.Inputs.PrayerUpdated(it) }
            )
        }

        is PrayerDetailsContract.Inputs.PrayerUpdated -> {
            updateState { it.copy(prayer = input.prayer) }
        }

        is PrayerDetailsContract.Inputs.GoBack -> {
            postEvent(PrayerDetailsContract.Events.NavigateBack)
        }

        is PrayerDetailsContract.Inputs.EditPrayer -> {
            postEvent(
                PrayerDetailsContract.Events.NavigateTo(
                    ScriptureNowRoute.PrayerEdit
                        .directions()
                        .path(getCurrentState().prayer.getCachedOrThrow().uuid.toString())
                        .build()
                )
            )
        }

        is PrayerDetailsContract.Inputs.DeletePrayer -> {
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
