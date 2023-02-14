package com.caseyjbrooks.scripturenow.viewmodel.prayer.edit

import com.caseyjbrooks.scripturenow.models.EditorMode
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.caseyjbrooks.scripturenow.models.routing.ScriptureNowRoute
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepository
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

public class CreateOrEditPrayerInputHandler(
    private val prayerRepository: PrayerRepository,
    private val fromJsonConverter: Converter<JsonElement, Prayer>,
    private val toJsonConverter: Converter<Prayer, JsonElement>,
) : InputHandler<
        CreateOrEditPrayerContract.Inputs,
        CreateOrEditPrayerContract.Events,
        CreateOrEditPrayerContract.State> {
    override suspend fun InputHandlerScope<
            CreateOrEditPrayerContract.Inputs,
            CreateOrEditPrayerContract.Events,
            CreateOrEditPrayerContract.State>.handleInput(
        input: CreateOrEditPrayerContract.Inputs
    ): Unit = when (input) {
        is CreateOrEditPrayerContract.Inputs.Initialize -> {
            if (input.prayerUuid == null) {
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
                    "saved prayer",
                    prayerRepository
                        .getPrayerById(input.prayerUuid)
                        .map { CreateOrEditPrayerContract.Inputs.SavedPrayerUpdated(it) },
                )
            }

            observeFlows(
                "schema definition",
                prayerRepository
                    .loadForm()
                    .map { CreateOrEditPrayerContract.Inputs.SchemaUpdated(it) },
            )
        }

        is CreateOrEditPrayerContract.Inputs.SchemaUpdated -> {
            updateState { it.copy(schema = input.schema) }
        }

        is CreateOrEditPrayerContract.Inputs.SavedPrayerUpdated -> {
            updateState { it.copy(savedPrayer = input.savedPrayer) }

            val value = input.savedPrayer.getValueOrNull()
            if (value != null) {
                updateState { it.copy(formData = toJsonConverter.convertValue(value)) }
            }

            Unit
        }

        is CreateOrEditPrayerContract.Inputs.UpdateFormData -> {
            updateState { it.copy(formData = input.updatedData) }
        }

        is CreateOrEditPrayerContract.Inputs.SavePrayer -> {
            val currentState = getCurrentState()

            var parsedPrayer = fromJsonConverter.convertValue(currentState.formData)
            if (currentState.editorMode == EditorMode.Edit) {
                parsedPrayer = parsedPrayer.copy(uuid = currentState.savedPrayer.getCachedOrThrow().uuid)
            }
            prayerRepository.createOrUpdatePrayer(parsedPrayer)

            postEvent(
                CreateOrEditPrayerContract.Events.NavigateTo(
                    ScriptureNowRoute.PrayerDetails
                        .directions()
                        .path(parsedPrayer.uuid.toString())
                        .build()
                )
            )
        }

        is CreateOrEditPrayerContract.Inputs.GoBack -> {
            postEvent(CreateOrEditPrayerContract.Events.NavigateUp)
        }
    }
}
