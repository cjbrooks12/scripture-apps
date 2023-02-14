package com.caseyjbrooks.scripturenow.ui.screens.prayer.edit

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
import com.caseyjbrooks.scripturenow.viewmodel.prayer.edit.CreateOrEditPrayerContract
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.forms.compose.form.MaterialForm

@Composable
public fun EditPrayerScreen(prayerId: String?) {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) {
        injector.getCreateOrEditPrayerViewModel(
            coroutineScope,
            prayerId,
        )
    }
    val vmState by vm.observeStates().collectAsState()

    EditPrayerScreen(vmState) { vm.trySend(it) }
}

@Composable
public fun EditPrayerScreen(
    state: CreateOrEditPrayerContract.State,
    postInput: (CreateOrEditPrayerContract.Inputs) -> Unit,
) {
    BottomBarLayout(
        title = { Text("Edit Prayer") },
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
                        postInput(CreateOrEditPrayerContract.Inputs.UpdateFormData(it))
                    },
                )

                Spacer(Modifier.weight(1f))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { postInput(CreateOrEditPrayerContract.Inputs.SavePrayer) },
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
