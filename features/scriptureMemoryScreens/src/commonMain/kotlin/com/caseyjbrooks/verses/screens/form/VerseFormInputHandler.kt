package com.caseyjbrooks.verses.screens.form

import com.caseyjbrooks.verses.domain.createFromText.CreateVerseFromTextUseCase
import com.caseyjbrooks.verses.domain.getbyid.GetVerseByIdUseCase
import com.caseyjbrooks.verses.domain.update.UpdateVerseUseCase
import com.caseyjbrooks.verses.models.VerseTag
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postEventWithState
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getValueOrNull
import kotlinx.coroutines.flow.map

internal class VerseFormInputHandler(
    private val getByIdUseCase: GetVerseByIdUseCase,
    private val createVerseUseCase: CreateVerseFromTextUseCase,
    private val updateVerseUseCase: UpdateVerseUseCase,
) : InputHandler<
        VerseFormContract.Inputs,
        VerseFormContract.Events,
        VerseFormContract.State,
        > {
    override suspend fun InputHandlerScope<
            VerseFormContract.Inputs,
            VerseFormContract.Events,
            VerseFormContract.State,
            >.handleInput(
        input: VerseFormContract.Inputs,
    ): Unit = when (input) {
        is VerseFormContract.Inputs.ObserveVerse -> {
            val currentState = updateStateAndGet { it.copy(verseId = input.verseId) }

            if (currentState.verseId == null) {
                updateState { it.copy(cachedVerse = Cached.FetchingFailed(NullPointerException(), null)) }
                cancelSideJob("ObserveVerse")
            } else {
                observeFlows(
                    "ObserveVerse",
                    getByIdUseCase(currentState.verseId)
                        .map { VerseFormContract.Inputs.VerseUpdated(it) },
                )
            }
        }

        is VerseFormContract.Inputs.VerseUpdated -> {
            val verse = input.cachedVerses.getCachedOrNull()
            updateState {
                it.copy(
                    cachedVerse = input.cachedVerses,
                    reference = verse?.reference ?: "",
                    verseText = verse?.text ?: "",
                    tags = verse?.tags?.map { it.tag }?.toSet() ?: emptySet()
                )
            }
        }

        is VerseFormContract.Inputs.ReferenceUpdated -> {
            updateState { it.copy(reference = input.text) }
        }
        is VerseFormContract.Inputs.VerseTextUpdated -> {
            updateState { it.copy(verseText = input.text) }
        }

        is VerseFormContract.Inputs.AddTag -> {
            updateState { it.copy(tags = it.tags + input.tag) }
        }

        is VerseFormContract.Inputs.RemoveTag -> {
            updateState { it.copy(tags = it.tags - input.tag) }
        }

        is VerseFormContract.Inputs.SaveVerse -> {
            val currentState = getCurrentState()
            val currentVerse = currentState.cachedVerse.getValueOrNull()

            val newVerse = if (currentVerse == null) {
                createVerseUseCase(
                    reference = currentState.reference,
                    text = currentState.verseText,
                    tags = currentState.tags,
                )
            } else {
                updateVerseUseCase(
                    currentVerse.copy(
                        reference = currentState.reference,
                        text = currentState.verseText,
                        tags = currentState.tags
                            .map { VerseTag(it) },
                    )
                )
            }

            postEvent(
                VerseFormContract.Events.NavigateTo(
                    VerseFormRoute.Directions.details(newVerse),
                    replaceTop = true,
                ),
            )
        }

        is VerseFormContract.Inputs.NavigateUp -> {
            postEventWithState { currentState ->
                val currentVerse = currentState.cachedVerse.getCachedOrNull()

                VerseFormContract.Events.NavigateTo(
                    if (currentVerse != null) {
                        VerseFormRoute.Directions.details(currentVerse)
                    } else {
                        VerseFormRoute.Directions.list()
                    },
                    replaceTop = true,
                )
            }
        }

        is VerseFormContract.Inputs.GoBack -> {
            postEvent(
                VerseFormContract.Events.NavigateBack,
            )
        }
    }
}
