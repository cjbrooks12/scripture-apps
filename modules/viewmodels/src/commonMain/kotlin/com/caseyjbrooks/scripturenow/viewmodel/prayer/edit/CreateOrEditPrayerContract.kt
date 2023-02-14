package com.caseyjbrooks.scripturenow.viewmodel.prayer.edit

import com.benasher44.uuid.Uuid
import com.caseyjbrooks.scripturenow.models.EditorMode
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.isLoading
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

public object CreateOrEditPrayerContract {
    public data class State(
        val editorMode: EditorMode = EditorMode.Create,
        val savedPrayer: Cached<Prayer> = Cached.NotLoaded(),

        val schema: Cached<Pair<JsonSchema, UiSchema>> = Cached.NotLoaded(),
        val formData: JsonElement = JsonNull,
    ) {
        val loading: Boolean = when (editorMode) {
            EditorMode.Create -> schema.isLoading()
            EditorMode.Edit -> schema.isLoading() || savedPrayer.isLoading()
        }
    }

    public sealed class Inputs {
        public data class Initialize(val prayerUuid: Uuid?) : Inputs()
        public data class SchemaUpdated(val schema: Cached<Pair<JsonSchema, UiSchema>>) : Inputs()
        public data class SavedPrayerUpdated(val savedPrayer: Cached<Prayer>) : Inputs()

        public data class UpdateFormData(val updatedData: JsonElement) : Inputs()

        public object SavePrayer : Inputs()
        public object GoBack : Inputs()
    }

    public sealed class Events {
        public data class NavigateTo(val destination: String) : Events()
        public object NavigateUp : Events()
    }
}
