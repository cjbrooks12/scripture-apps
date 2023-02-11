package com.caseyjbrooks.scripturenow.viewmodel.memory.edit

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.EditorMode
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import kotlinx.datetime.LocalDateTime

public object CreateOrEditMemoryVerseContract {
    public data class State(
        val editorMode: EditorMode = EditorMode.Create,
        val loading: Boolean = false,
        val lastSavedOn: LocalDateTime? = null,
        val hasUnsavedChanges: Boolean = false,
        val savedVerse: MemoryVerse? = null,
        val editingVerse: MemoryVerse? = null,
    )

    public sealed class Inputs {
        public data class Initialize(val verseUuid: Uuid?) : Inputs()
        public data class UpdateVerse(val updatedVerse: MemoryVerse) : Inputs()
        public object SaveVerse : Inputs()
        public object GoBack : Inputs()
    }

    public sealed class Events {
        public object NavigateUp : Events()
    }
}
