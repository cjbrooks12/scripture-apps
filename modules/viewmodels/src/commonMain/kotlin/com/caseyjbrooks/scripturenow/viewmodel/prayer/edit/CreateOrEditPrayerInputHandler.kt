package com.caseyjbrooks.scripturenow.viewmodel.prayer.edit

import com.benasher44.uuid.uuid4
import com.caseyjbrooks.scripturenow.models.EditorMode
import com.caseyjbrooks.scripturenow.models.prayer.Prayer
import com.caseyjbrooks.scripturenow.repositories.prayer.PrayerRepository
import com.caseyjbrooks.scripturenow.utils.now
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.repository.cache.awaitValue
import com.copperleaf.ballast.repository.cache.getValueOrThrow
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime

public class CreateOrEditPrayerInputHandler(
    private val prayerRepository: PrayerRepository
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
            if (input.verseUuid == null) {
                updateState {
                    it.copy(
                        editorMode = EditorMode.Create,
                        loading = false,
                        savedVerse = null,
                        editingVerse = Prayer(
                            uuid = uuid4(), // create a new UUID for this verse
                            text = "",
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now(),
                        )
                    )
                }
            } else {
                updateState {
                    it.copy(
                        editorMode = EditorMode.Edit,
                        loading = true,
                        savedVerse = null,
                        editingVerse = null,
                    )
                }

                val savedVerse: Prayer = prayerRepository
                    .getPrayer(input.verseUuid)
                    .awaitValue()
                    .getValueOrThrow()

                updateState {
                    it.copy(
                        loading = false,
                        savedVerse = savedVerse,
                        editingVerse = savedVerse.copy(),
                    )
                }
            }
        }

        is CreateOrEditPrayerContract.Inputs.UpdateVerse -> {
            val currentState = updateStateAndGet { it.copy(editingVerse = input.updatedVerse) }
            if (currentState.editorMode == EditorMode.Edit) {
                updateState { it.copy(hasUnsavedChanges = true) }

                sideJob("autosave") {
                    delay(3000)
                    postInput(CreateOrEditPrayerContract.Inputs.SaveVerse)
                }
            }

            Unit
        }

        is CreateOrEditPrayerContract.Inputs.SaveVerse -> {
            val currentState =
                updateStateAndGet { it.copy(lastSavedOn = LocalDateTime.now(), hasUnsavedChanges = false) }

            prayerRepository.createOrUpdatePrayer(currentState.editingVerse!!)

            if (currentState.editorMode == EditorMode.Create) {
                postEvent(CreateOrEditPrayerContract.Events.NavigateUp)
            }

            Unit
        }

        is CreateOrEditPrayerContract.Inputs.GoBack -> {
            val currentState = getCurrentState()

            if (currentState.hasUnsavedChanges) {
                prayerRepository.createOrUpdatePrayer(currentState.editingVerse!!)
            }

            postEvent(CreateOrEditPrayerContract.Events.NavigateUp)
        }
    }
}
