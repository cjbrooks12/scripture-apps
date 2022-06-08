package com.copperleaf.scripturenow.ui.verses.edit

import com.benasher44.uuid.uuid4
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.repository.cache.awaitValue
import com.copperleaf.ballast.repository.cache.getValueOrThrow
import com.copperleaf.scripturenow.common.now
import com.copperleaf.scripturenow.repositories.verses.MemoryVersesRepository
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import com.copperleaf.scripturenow.utils.EditorMode
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime

class CreateOrEditMemoryVerseInputHandler(
    private val memoryVersesRepository: MemoryVersesRepository
) : InputHandler<
    CreateOrEditMemoryVerseContract.Inputs,
    CreateOrEditMemoryVerseContract.Events,
    CreateOrEditMemoryVerseContract.State> {
    override suspend fun InputHandlerScope<
        CreateOrEditMemoryVerseContract.Inputs,
        CreateOrEditMemoryVerseContract.Events,
        CreateOrEditMemoryVerseContract.State>.handleInput(
        input: CreateOrEditMemoryVerseContract.Inputs
    ) = when (input) {
        is CreateOrEditMemoryVerseContract.Inputs.Initialize -> {
            if (input.verseUuid == null) {
                updateState {
                    it.copy(
                        editorMode = EditorMode.Create,
                        loading = false,
                        savedVerse = null,
                        editingVerse = MemoryVerse(
                            uuid = uuid4(), // create a new UUID for this verse
                            main = false,
                            text = "",
                            reference = "",
                            version = "",
                            verseUrl = "",
                            notice = "",
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now(),
                        )
                    )
                }
            } else {
                updateState {
                    it.copy(
                        editorMode = EditorMode.Edit,
                        loading = true,
                        savedVerse = null,
                        editingVerse = null,
                    )
                }

                val savedVerse: MemoryVerse = memoryVersesRepository
                    .getVerse(input.verseUuid)
                    .awaitValue()
                    .getValueOrThrow()

                updateState {
                    it.copy(
                        loading = false,
                        savedVerse = savedVerse,
                        editingVerse = savedVerse.copy(),
                    )
                }
            }
        }
        is CreateOrEditMemoryVerseContract.Inputs.UpdateVerse -> {
            val currentState = updateStateAndGet { it.copy(editingVerse = input.updatedVerse) }
            if (currentState.editorMode == EditorMode.Edit) {
                updateState { it.copy(hasUnsavedChanges = true) }

                sideJob("autosave") {
                    delay(3000)
                    postInput(CreateOrEditMemoryVerseContract.Inputs.SaveVerse)
                }
            }

            Unit
        }
        is CreateOrEditMemoryVerseContract.Inputs.SaveVerse -> {
            val currentState = updateStateAndGet { it.copy(lastSavedOn = LocalDateTime.now(), hasUnsavedChanges = false) }

            memoryVersesRepository.createOrUpdateVerse(currentState.editingVerse!!)

            if (currentState.editorMode == EditorMode.Create) {
                postEvent(CreateOrEditMemoryVerseContract.Events.NavigateUp)
            }

            Unit
        }
        is CreateOrEditMemoryVerseContract.Inputs.GoBack -> {
            val currentState = getCurrentState()

            if(currentState.hasUnsavedChanges) {
                memoryVersesRepository.createOrUpdateVerse(currentState.editingVerse!!)
            }

            postEvent(CreateOrEditMemoryVerseContract.Events.NavigateUp)
        }
    }
}
