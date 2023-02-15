package com.caseyjbrooks.scripturenow.viewmodel.memory.detail

import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import kotlinx.coroutines.flow.map

public class MemoryVerseDetailsInputHandler(
    private val memoryVerseRepository: MemoryVerseRepository
) : InputHandler<
        MemoryVerseDetailsContract.Inputs,
        MemoryVerseDetailsContract.Events,
        MemoryVerseDetailsContract.State> {
    override suspend fun InputHandlerScope<
            MemoryVerseDetailsContract.Inputs,
            MemoryVerseDetailsContract.Events,
            MemoryVerseDetailsContract.State>.handleInput(
        input: MemoryVerseDetailsContract.Inputs
    ): Unit = when (input) {
        is MemoryVerseDetailsContract.Inputs.Initialize -> {
            observeFlows(
                "focused memory verse",
                memoryVerseRepository
                    .getVerseById(input.verseUuid)
                    .map { MemoryVerseDetailsContract.Inputs.MemoryVerseUpdated(it) }
            )
        }

        is MemoryVerseDetailsContract.Inputs.MemoryVerseUpdated -> {
            updateState { it.copy(memoryVerse = input.memoryVerse) }
        }

        is MemoryVerseDetailsContract.Inputs.GoBack -> {
            postEvent(MemoryVerseDetailsContract.Events.NavigateBack)
        }

        is MemoryVerseDetailsContract.Inputs.SetAsMainVerse -> {
            val verse = getCurrentState().memoryVerse.getCachedOrNull()

            if (verse == null) {
                noOp()
            } else {
                sideJob("setting as main verse") {
                    memoryVerseRepository.setAsMainMemoryVerse(verse)
                }
            }
        }

        is MemoryVerseDetailsContract.Inputs.EditVerse -> {
            postEvent(
                MemoryVerseDetailsContract.Events.NavigateTo(
                    ScriptureNowRoute.MemoryVerseEdit
                        .directions()
                        .path(getCurrentState().memoryVerse.getCachedOrThrow().uuid.toString())
                        .build()
                )
            )
        }

        is MemoryVerseDetailsContract.Inputs.DeleteVerse -> {
            val cachedVerse = getCurrentState().memoryVerse.getCachedOrNull()

            if (cachedVerse != null) {
                memoryVerseRepository.deleteVerse(cachedVerse)
                postEvent(MemoryVerseDetailsContract.Events.NavigateBack)
            } else {
                noOp()
            }
        }
    }
}
