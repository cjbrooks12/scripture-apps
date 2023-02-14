package com.caseyjbrooks.scripturenow.repositories

import android.content.Context
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.forms.core.ui.UiSchema
import com.copperleaf.json.schema.JsonSchema
import com.copperleaf.json.utils.parseJson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public class AndroidAssetsFormLoader(
    val applicationContext: Context,
    val folderPath: String,
) : FormLoader {
    override fun loadForm(): Flow<Cached<Pair<JsonSchema, UiSchema>>> {
        return flow {
            try {
                val schema = applicationContext.assets
                    .open("forms/$folderPath/schema.json")
                    .bufferedReader()
                    .readText()
                    .parseJson()
                    .let { JsonSchema.parse(it) }

                val uiSchema = applicationContext.assets
                    .open("forms/$folderPath/uiSchema.json")
                    .bufferedReader()
                    .readText()
                    .parseJson()
                    .let { UiSchema.parse(schema, it) }

                emit(Cached.Value(schema to uiSchema))
            } catch (e: Exception) {
                emit(Cached.FetchingFailed(e, null))
            }
        }
    }
}
