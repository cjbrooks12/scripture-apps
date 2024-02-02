package com.caseyjbrooks.verses.screens.list

import com.caseyjbrooks.verses.domain.archive.ArchiveVerseUseCase
import com.caseyjbrooks.verses.domain.query.QueryVersesUseCase
import com.caseyjbrooks.verses.domain.restore.RestoreArchivedVerseUseCase
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postInput
import kotlinx.coroutines.flow.map

internal class VerseListInputHandler(
    private val queryVersesUseCase: QueryVersesUseCase,
    private val archiveVerseUseCase: ArchiveVerseUseCase,
    private val restoreArchivedVerseUseCase: RestoreArchivedVerseUseCase,
) : InputHandler<
        VerseListContract.Inputs,
        VerseListContract.Events,
        VerseListContract.State,
        > {
    override suspend fun InputHandlerScope<
            VerseListContract.Inputs,
            VerseListContract.Events,
            VerseListContract.State,
            >.handleInput(
        input: VerseListContract.Inputs,
    ): Unit = when (input) {
        is VerseListContract.Inputs.ObserveVerseList -> {
            val currentState = getCurrentState()
            observeFlows(
                "ObserveVerseList",
                queryVersesUseCase
                    .invoke(
                        currentState.archiveStatus,
                        currentState.tagFilter.toSet(),
                    )
                    .map { VerseListContract.Inputs.VersesUpdated(it) },
            )
        }

        is VerseListContract.Inputs.VersesUpdated -> {
            updateState { it.copy(cachedVerses = input.cachedVerses) }
        }

        is VerseListContract.Inputs.SetArchiveStatus -> {
            updateState { it.copy(archiveStatus = input.archiveStatus) }
            postInput(VerseListContract.Inputs.ObserveVerseList)
        }

        is VerseListContract.Inputs.AddTagFilter -> {
            updateState { it.copy(tagFilter = (it.tagFilter + input.tag).sortedBy { it.tag }) }
            postInput(VerseListContract.Inputs.ObserveVerseList)
        }

        is VerseListContract.Inputs.RemoveTagFilter -> {
            updateState { it.copy(tagFilter = (it.tagFilter - input.tag).sortedBy { it.tag }) }
            postInput(VerseListContract.Inputs.ObserveVerseList)
        }

        is VerseListContract.Inputs.CreateNewVerse -> {
            postEvent(
                VerseListContract.Events.NavigateTo(
                    VerseListRoute.Directions.new(),
                ),
            )
        }

        is VerseListContract.Inputs.ViewVerseDetails -> {
            postEvent(
                VerseListContract.Events.NavigateTo(
                    VerseListRoute.Directions.view(input.verse),
                ),
            )
        }

        is VerseListContract.Inputs.EditVerse -> {
            postEvent(
                VerseListContract.Events.NavigateTo(
                    VerseListRoute.Directions.edit(input.verse),
                ),
            )
        }

        is VerseListContract.Inputs.Practice -> {
            postEvent(
                VerseListContract.Events.NavigateTo(
                    VerseListRoute.Directions.practice(input.verse),
                ),
            )
        }
        is VerseListContract.Inputs.Archive -> {
            sideJob("Archive") {
                archiveVerseUseCase(input.verse)
            }
        }
        is VerseListContract.Inputs.RestoreFromArchive -> {
            sideJob("RestoreFromArchive") {
                restoreArchivedVerseUseCase(input.verse)
            }
        }

        is VerseListContract.Inputs.GoBack -> {
            postEvent(
                VerseListContract.Events.NavigateBack,
            )
        }
    }
}
