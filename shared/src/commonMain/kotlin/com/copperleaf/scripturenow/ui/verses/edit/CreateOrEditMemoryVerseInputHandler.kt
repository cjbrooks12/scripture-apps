package com.copperleaf.scripturenow.ui.verses.edit

import com.benasher44.uuid.uuid4
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.repository.cache.awaitValue
import com.copperleaf.ballast.repository.cache.getValueOrThrow
import com.copperleaf.scripturenow.repositories.verses.MemoryVersesRepository
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import com.copperleaf.scripturenow.utils.EditorMode

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
                        edits = MemoryVerse(
                            uuid = uuid4(), // create a new UUID for this verse
                            main = false,
                            text = "",
                            reference = "",
                            version = "",
                            verseUrl = "",
                            notice = "",
                        )
                    )
                }
            } else {
                updateState {
                    it.copy(
                        editorMode = EditorMode.Create,
                        loading = true,
                        savedVerse = null,
                        edits = null,
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
                        edits = savedVerse.copy(),
                    )
                }
            }
        }
        is CreateOrEditMemoryVerseContract.Inputs.GoBack -> {
            postEvent(CreateOrEditMemoryVerseContract.Events.NavigateUp)
        }
    }
}
