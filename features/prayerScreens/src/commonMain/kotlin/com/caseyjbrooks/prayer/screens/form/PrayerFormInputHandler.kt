package com.caseyjbrooks.prayer.screens.form

import com.caseyjbrooks.prayer.domain.createFromText.CreatePrayerFromTextUseCase
import com.caseyjbrooks.prayer.domain.getbyid.GetPrayerByIdUseCase
import com.caseyjbrooks.prayer.domain.update.UpdatePrayerUseCase
import com.caseyjbrooks.prayer.models.PrayerNotification
import com.caseyjbrooks.prayer.models.PrayerTag
import com.caseyjbrooks.prayer.models.SavedPrayerType
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postEventWithState
import com.copperleaf.ballast.repository.cache.Cached
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.getValueOrNull
import kotlinx.coroutines.flow.map

internal class PrayerFormInputHandler(
    private val getByIdUseCase: GetPrayerByIdUseCase,
    private val createPrayerUseCase: CreatePrayerFromTextUseCase,
    private val updatePrayerUseCase: UpdatePrayerUseCase,
) : InputHandler<
        PrayerFormContract.Inputs,
        PrayerFormContract.Events,
        PrayerFormContract.State,
        > {
    override suspend fun InputHandlerScope<
            PrayerFormContract.Inputs,
            PrayerFormContract.Events,
            PrayerFormContract.State,
            >.handleInput(
        input: PrayerFormContract.Inputs,
    ): Unit = when (input) {
        is PrayerFormContract.Inputs.ObservePrayer -> {
            val currentState = updateStateAndGet { it.copy(prayerId = input.prayerId) }

            if (currentState.prayerId == null) {
                updateState { it.copy(cachedPrayer = Cached.FetchingFailed(NullPointerException(), null)) }
                cancelSideJob("ObservePrayer")
            } else {
                observeFlows(
                    "ObservePrayer",
                    getByIdUseCase(currentState.prayerId)
                        .map { PrayerFormContract.Inputs.PrayerUpdated(it) },
                )
            }
        }

        is PrayerFormContract.Inputs.PrayerUpdated -> {
            val prayer = input.cachedPrayers.getCachedOrNull()
            updateState {
                it.copy(
                    cachedPrayer = input.cachedPrayers,
                    prayerText = prayer?.text ?: "",
                    completionDate = prayer?.prayerType?.let {
                        when (it) {
                            is SavedPrayerType.Persistent -> null
                            is SavedPrayerType.ScheduledCompletable -> it.completionDate
                        }
                    },
                    notification = prayer?.notification ?: PrayerNotification.None,
                    tags = prayer?.tags?.map { it.tag }?.toSet() ?: emptySet()
                )
            }
        }

        is PrayerFormContract.Inputs.PrayerTextUpdated -> {
            updateState { it.copy(prayerText = input.text) }
        }

        is PrayerFormContract.Inputs.CompletionDateUpdated -> {
            updateState { it.copy(completionDate = input.completionDate) }
        }
        is PrayerFormContract.Inputs.PrayerNotificationUpdated -> {
            updateState { it.copy(notification = input.notification) }
        }

        is PrayerFormContract.Inputs.AddTag -> {
            updateState { it.copy(tags = it.tags + input.tag) }
        }

        is PrayerFormContract.Inputs.RemoveTag -> {
            updateState { it.copy(tags = it.tags - input.tag) }
        }

        is PrayerFormContract.Inputs.SavePrayer -> {
            val currentState = getCurrentState()
            val currentPrayer = currentState.cachedPrayer.getValueOrNull()

            val newPrayer = if (currentPrayer == null) {
                createPrayerUseCase(
                    text = currentState.prayerText,
                    completionDate = currentState.completionDate,
                    tags = currentState.tags,
                )
            } else {
                updatePrayerUseCase(
                    currentPrayer.copy(
                        text = currentState.prayerText,
                        prayerType = currentState.completionDate
                            ?.let { SavedPrayerType.ScheduledCompletable(it) }
                            ?: SavedPrayerType.Persistent,
                        tags = currentState.tags
                            .map { PrayerTag(it) },
                    )
                )
            }

            postEvent(
                PrayerFormContract.Events.NavigateTo(
                    PrayerFormRoute.Directions.details(newPrayer),
                    replaceTop = true,
                ),
            )
        }

        is PrayerFormContract.Inputs.NavigateUp -> {
            postEventWithState { currentState ->
                val currentPrayer = currentState.cachedPrayer.getCachedOrNull()

                PrayerFormContract.Events.NavigateTo(
                    if (currentPrayer != null) {
                        PrayerFormRoute.Directions.details(currentPrayer)
                    } else {
                        PrayerFormRoute.Directions.list()
                    },
                    replaceTop = true,
                )
            }
        }

        is PrayerFormContract.Inputs.GoBack -> {
            postEvent(
                PrayerFormContract.Events.NavigateBack,
            )
        }
    }
}
