package com.caseyjbrooks.scripturenow.viewmodel.memory.edit

import com.caseyjbrooks.scripturenow.models.EditorMode
import com.caseyjbrooks.scripturenow.models.memory.MemoryVerse
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.repositories.memory.MemoryVerseRepository
import com.caseyjbrooks.scripturenow.utils.converter.Converter
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.navigation.routing.build
import com.copperleaf.ballast.navigation.routing.directions
import com.copperleaf.ballast.navigation.routing.path
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.repository.cache.getCachedOrThrow
import com.copperleaf.ballast.repository.cache.getValueOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonElement

public class CreateOrEditMemoryVerseInputHandler(
    private val memoryVerseRepository: MemoryVerseRepository,
    private val fromJsonConverter: Converter<JsonElement, MemoryVerse>,
    private val toJsonConverter: Converter<MemoryVerse, JsonElement>,
) : InputHandler<
        CreateOrEditMemoryVerseContract.Inputs,
        CreateOrEditMemoryVerseContract.Events,
        CreateOrEditMemoryVerseContract.State> {
    override suspend fun InputHandlerScope<
            CreateOrEditMemoryVerseContract.Inputs,
            CreateOrEditMemoryVerseContract.Events,
            CreateOrEditMemoryVerseContract.State>.handleInput(
        input: CreateOrEditMemoryVerseContract.Inputs
    ): Unit = when (input) {
        is CreateOrEditMemoryVerseContract.Inputs.Initialize -> {
            if (input.verseUuid == null) {
                updateState {
                    it.copy(
                        editorMode = EditorMode.Create,
                    )
                }
            } else {
                updateState {
                    it.copy(
                        editorMode = EditorMode.Edit,
                    )
                }

                observeFlows(
                    "saved verse",
                    memoryVerseRepository
                        .getVerseById(input.verseUuid)
                        .map { CreateOrEditMemoryVerseContract.Inputs.SavedVerseUpdated(it) },
                )
            }

            observeFlows(
                "schema definition",
                memoryVerseRepository
                    .loadForm()
                    .map { CreateOrEditMemoryVerseContract.Inputs.SchemaUpdated(it) },
            )
        }

        is CreateOrEditMemoryVerseContract.Inputs.SchemaUpdated -> {
            updateState { it.copy(schema = input.schema) }
        }

        is CreateOrEditMemoryVerseContract.Inputs.SavedVerseUpdated -> {
            updateState { it.copy(savedVerse = input.savedVerse) }

            val value = input.savedVerse.getValueOrNull()
            if (value != null) {
                updateState { it.copy(formData = toJsonConverter.convertValue(value)) }
            }

            Unit
        }

        is CreateOrEditMemoryVerseContract.Inputs.UpdateFormData -> {
            updateState { it.copy(formData = input.updatedData) }
        }

        is CreateOrEditMemoryVerseContract.Inputs.SaveVerse -> {
            val currentState = getCurrentState()

            var parsedVerse = fromJsonConverter.convertValue(currentState.formData)
            if (currentState.editorMode == EditorMode.Edit) {
                parsedVerse = parsedVerse.copy(uuid = currentState.savedVerse.getCachedOrThrow().uuid)
            }
            memoryVerseRepository.createOrUpdateVerse(parsedVerse)

            postEvent(
                CreateOrEditMemoryVerseContract.Events.NavigateTo(
                    ScriptureNowRoute.MemoryVerseDetails
                        .directions()
                        .path(parsedVerse.uuid.toString())
                        .build()
                )
            )
        }

        is CreateOrEditMemoryVerseContract.Inputs.GoBack -> {
            postEvent(CreateOrEditMemoryVerseContract.Events.NavigateUp)
        }
    }
}
