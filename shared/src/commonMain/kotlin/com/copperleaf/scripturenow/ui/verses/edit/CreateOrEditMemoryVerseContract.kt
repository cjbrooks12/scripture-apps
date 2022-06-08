package com.copperleaf.scripturenow.ui.verses.edit

import com.benasher44.uuid.Uuid
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import com.copperleaf.scripturenow.utils.EditorMode
import kotlinx.datetime.LocalDateTime

object CreateOrEditMemoryVerseContract {
    data class State(
        val editorMode: EditorMode = EditorMode.Create,
        val loading: Boolean = false,
        val lastSavedOn: LocalDateTime? = null,
        val hasUnsavedChanges: Boolean = false,
        val savedVerse: MemoryVerse? = null,
        val editingVerse: MemoryVerse? = null,
    )

    sealed class Inputs {
        data class Initialize(val verseUuid: Uuid?) : Inputs()
        data class UpdateVerse(val updatedVerse: MemoryVerse) : Inputs()
        object SaveVerse : Inputs()
        object GoBack : Inputs()
    }

    sealed class Events {
        object NavigateUp : Events()
    }
}
