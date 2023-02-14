package com.caseyjbrooks.scripturenow.ui.screens.memory.edit

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.caseyjbrooks.scripturenow.models.EditorMode
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.BottomBarLayout
import com.caseyjbrooks.scripturenow.ui.layouts.ScrollableContent
import com.caseyjbrooks.scripturenow.viewmodel.memory.edit.CreateOrEditMemoryVerseContract
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.forms.compose.form.MaterialForm

@Composable
public fun EditMemoryVerseScreen(verseId: String?) {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) {
        injector.getCreateOrEditMemoryVerseViewModel(
            coroutineScope,
            verseId,
        )
    }
    val vmState by vm.observeStates().collectAsState()

    EditMemoryVerseScreen(vmState) { vm.trySend(it) }
}

@Composable
public fun EditMemoryVerseScreen(
    state: CreateOrEditMemoryVerseContract.State,
    postInput: (CreateOrEditMemoryVerseContract.Inputs) -> Unit,
) {
    BottomBarLayout(
        title = { Text("Edit Verse") },
    ) {
        ScrollableContent {
            if (state.loading) {
                CircularProgressIndicator()
            } else {
                val (schema, uiSchema) = state.schema.getCachedOrThrow()
                MaterialForm(
                    schema = schema,
                    uiSchema = uiSchema,
                    data = state.formData,
                    onDataChanged = {
                        postInput(CreateOrEditMemoryVerseContract.Inputs.UpdateFormData(it))
                    },
                )

                Spacer(Modifier.weight(1f))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { postInput(CreateOrEditMemoryVerseContract.Inputs.SaveVerse) },
                ) {
                    if (state.editorMode == EditorMode.Create) {
                        Text("Create")
                    } else {
                        Text("Save Changes")
                    }
                }
            }
        }
    }
}
