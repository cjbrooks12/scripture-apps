package com.caseyjbrooks.scripturenow.viewmodel.prayer.edit

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.EditorMode
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import kotlinx.datetime.LocalDateTime

public object CreateOrEditPrayerContract {
    public data class State(
        val editorMode: EditorMode = EditorMode.Create,
        val loading: Boolean = false,
        val lastSavedOn: LocalDateTime? = null,
        val hasUnsavedChanges: Boolean = false,
        val savedVerse: Prayer? = null,
        val editingVerse: Prayer? = null,
    )

    public sealed class Inputs {
        public data class Initialize(val verseUuid: Uuid?) : Inputs()
        public data class UpdateVerse(val updatedVerse: Prayer) : Inputs()
        public object SaveVerse : Inputs()
        public object GoBack : Inputs()
    }

    public sealed class Events {
        public object NavigateUp : Events()
    }
}
