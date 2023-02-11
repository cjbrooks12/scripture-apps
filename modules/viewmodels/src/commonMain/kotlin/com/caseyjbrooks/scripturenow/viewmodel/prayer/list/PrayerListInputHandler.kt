package com.caseyjbrooks.scripturenow.viewmodel.prayer.list

import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
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
                "focused memory verse",
                prayerRepository
                    .getAllPrayers(input.forceRefresh)
                    .map { PrayerListContract.Inputs.VersesUpdated(it) }
            )
        }

        is PrayerListContract.Inputs.VersesUpdated -> {
            updateState { it.copy(verses = input.verses) }
        }

        is PrayerListContract.Inputs.CreateVerse -> {
//            postEvent(
//                PrayerListContract.Events.NavigateTo(
//                    Destinations.App.Verses.Create.directions()
//                )
//            )
        }

        is PrayerListContract.Inputs.ViewVerse -> {
//            postEvent(
//                PrayerListContract.Events.NavigateTo(
//                    Destinations.App.Verses.Detail.directions(
//                        pathParameters = mapOf(
//                            "verseId" to listOf(input.verse.uuid.toString())
//                        )
//                    )
//                )
//            )
        }

        is PrayerListContract.Inputs.EditVerse -> {
//            postEvent(
//                PrayerListContract.Events.NavigateTo(
//                    Destinations.App.Verses.Edit.directions(
//                        pathParameters = mapOf(
//                            "verseId" to listOf(input.verse.uuid.toString())
//                        )
//                    )
//                )
//            )
        }

        is PrayerListContract.Inputs.DeleteVerse -> {
            // delete the verse
            prayerRepository.deletePrayer(input.verse)

            // then exit the screen
            postEvent(PrayerListContract.Events.NavigateBack)
        }
    }
}
