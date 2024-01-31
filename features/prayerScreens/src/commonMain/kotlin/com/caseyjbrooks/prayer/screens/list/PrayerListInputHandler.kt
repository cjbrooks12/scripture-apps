package com.caseyjbrooks.prayer.screens.list

import com.caseyjbrooks.prayer.domain.archive.ArchivePrayerUseCase
import com.caseyjbrooks.prayer.domain.query.QueryPrayersUseCase
import com.caseyjbrooks.prayer.domain.restore.RestoreArchivedPrayerUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postInput
import kotlinx.coroutines.flow.map

internal class PrayerListInputHandler(
    private val queryPrayersUseCase: QueryPrayersUseCase,
    private val archivePrayerUseCase: ArchivePrayerUseCase,
    private val restoreArchivedPrayerUseCase: RestoreArchivedPrayerUseCase,
) : InputHandler<
        PrayerListContract.Inputs,
        PrayerListContract.Events,
        PrayerListContract.State,
        > {
    override suspend fun InputHandlerScope<
            PrayerListContract.Inputs,
            PrayerListContract.Events,
            PrayerListContract.State,
            >.handleInput(
        input: PrayerListContract.Inputs,
    ): Unit = when (input) {
        is PrayerListContract.Inputs.ObservePrayerList -> {
            val currentState = getCurrentState()
            observeFlows(
                "ObservePrayerList",
                queryPrayersUseCase
                    .invoke(
                        currentState.archiveStatus,
                        currentState.prayerTypeFilter,
                        currentState.tagFilter.toSet(),
                    )
                    .map { PrayerListContract.Inputs.PrayersUpdated(it) },
            )
        }

        is PrayerListContract.Inputs.PrayersUpdated -> {
            updateState { it.copy(cachedPrayers = input.cachedPrayers) }
        }

        is PrayerListContract.Inputs.SetArchiveStatus -> {
            updateState { it.copy(archiveStatus = input.archiveStatus) }
            postInput(PrayerListContract.Inputs.ObservePrayerList)
        }

        is PrayerListContract.Inputs.AddTagFilter -> {
            updateState { it.copy(tagFilter = (it.tagFilter + input.tag).sortedBy { it.tag }) }
            postInput(PrayerListContract.Inputs.ObservePrayerList)
        }

        is PrayerListContract.Inputs.RemoveTagFilter -> {
            updateState { it.copy(tagFilter = (it.tagFilter - input.tag).sortedBy { it.tag }) }
            postInput(PrayerListContract.Inputs.ObservePrayerList)
        }

        is PrayerListContract.Inputs.CreateNewPrayer -> {
            postEvent(
                PrayerListContract.Events.NavigateTo(
                    PrayerListRoute.Directions.new(),
                ),
            )
        }

        is PrayerListContract.Inputs.ViewPrayerDetails -> {
            postEvent(
                PrayerListContract.Events.NavigateTo(
                    PrayerListRoute.Directions.view(input.prayer),
                ),
            )
        }

        is PrayerListContract.Inputs.EditPrayer -> {
            postEvent(
                PrayerListContract.Events.NavigateTo(
                    PrayerListRoute.Directions.edit(input.prayer),
                ),
            )
        }

        is PrayerListContract.Inputs.PrayNow -> {
            postEvent(
                PrayerListContract.Events.NavigateTo(
                    PrayerListRoute.Directions.timer(input.prayer),
                ),
            )
        }
        is PrayerListContract.Inputs.Archive -> {
            sideJob("Archive") {
                archivePrayerUseCase(input.prayer)
            }
        }
        is PrayerListContract.Inputs.RestoreFromArchive -> {
            sideJob("RestoreFromArchive") {
                restoreArchivedPrayerUseCase(input.prayer)
            }
        }

        is PrayerListContract.Inputs.GoBack -> {
            postEvent(
                PrayerListContract.Events.NavigateBack,
            )
        }
    }
}
