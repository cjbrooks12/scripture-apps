package com.caseyjbrooks.scripturenow.viewmodel.prayer.list

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
import com.copperleaf.ballast.observeFlows
import kotlinx.coroutines.flow.map

public class PrayerListInputHandler(
    private val prayerRepository: PrayerRepository
) : InputHandler<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State> {
    override suspend fun InputHandlerScope<
            PrayerListContract.Inputs,
            PrayerListContract.Events,
            PrayerListContract.State>.handleInput(
        input: PrayerListContract.Inputs
    ): Unit = when (input) {
        is PrayerListContract.Inputs.Initialize -> {
            observeFlows(
                "prayers list",
                prayerRepository
                    .getAllPrayers(input.forceRefresh)
                    .map { PrayerListContract.Inputs.PrayersUpdated(it) }
            )
        }

        is PrayerListContract.Inputs.PrayersUpdated -> {
            updateState { it.copy(prayers = input.prayers) }
        }

        is PrayerListContract.Inputs.CreatePrayer -> {
            postEvent(
                PrayerListContract.Events.NavigateTo(
                    ScriptureNowRoute.PrayerCreate
                        .directions()
                        .build()
                )
            )
        }

        is PrayerListContract.Inputs.ViewPrayer -> {
            postEvent(
                PrayerListContract.Events.NavigateTo(
                    ScriptureNowRoute.PrayerDetails
                        .directions()
                        .path(input.prayer.uuid.toString())
                        .build()
                )
            )
        }

        is PrayerListContract.Inputs.EditPrayer -> {
            postEvent(
                PrayerListContract.Events.NavigateTo(
                    ScriptureNowRoute.PrayerEdit
                        .directions()
                        .path(input.prayer.uuid.toString())
                        .build()
                )
            )
        }

        is PrayerListContract.Inputs.DeletePrayer -> {
            // delete the prayer
            prayerRepository.deletePrayer(input.prayer)
        }
    }
}
