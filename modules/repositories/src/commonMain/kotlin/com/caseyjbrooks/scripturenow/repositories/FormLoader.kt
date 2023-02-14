package com.caseyjbrooks.scripturenow.repositories

import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import kotlinx.coroutines.flow.Flow

public interface FormLoader {
    public fun loadForm(): Flow<Cached<Pair<JsonSchema, UiSchema>>>
}
