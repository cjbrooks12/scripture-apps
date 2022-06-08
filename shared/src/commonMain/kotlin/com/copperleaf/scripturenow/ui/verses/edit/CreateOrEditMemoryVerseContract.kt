package com.copperleaf.scripturenow.ui.verses.edit

import com.benasher44.uuid.Uuid
import com.copperleaf.scripturenow.repositories.verses.models.MemoryVerse
import com.copperleaf.scripturenow.utils.EditorMode

object CreateOrEditMemoryVerseContract {
    data class State(
        val editorMode: EditorMode = EditorMode.Create,
        val loading: Boolean = false,
        val savedVerse: MemoryVerse? = null,
        val edits: MemoryVerse? = null,
    )

    sealed class Inputs {
        data class Initialize(val verseUuid: Uuid?) : Inputs()
        object GoBack : Inputs()
    }

    sealed class Events {
        object NavigateUp : Events()
    }
}
