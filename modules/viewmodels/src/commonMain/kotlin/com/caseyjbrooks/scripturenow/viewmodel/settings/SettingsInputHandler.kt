package com.caseyjbrooks.scripturenow.viewmodel.settings

import com.caseyjbrooks.scripturenow.db.preferences.AppPreferences
import com.caseyjbrooks.scripturenow.repositories.auth.AuthRepository
import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postEventWithState
import kotlinx.coroutines.flow.map

public class SettingsInputHandler(
    private val authRepository: AuthRepository,
    private val appPreferences: AppPreferences,
) : InputHandler<
        SettingsContract.Inputs,
        SettingsContract.Events,
        SettingsContract.State> {
    override suspend fun InputHandlerScope<
            SettingsContract.Inputs,
            SettingsContract.Events,
            SettingsContract.State>.handleInput(
        input: SettingsContract.Inputs
    ): Unit = when (input) {
        is SettingsContract.Inputs.Initialize -> {
            updateState {
                it.copy(
                    showReviewPrompt = true,
                    updateIsAvailable = true,
                    currentVersion = "0.1.0",
                    latestVersion = "0.2.1",
                )
            }

            observeFlows(
                "auth state",
                authRepository
                    .getAuthState(input.forceRefresh)
                    .map { SettingsContract.Inputs.AuthStateChanged(it) },
                appPreferences
                    .getShowMainVerse()
                    .map { SettingsContract.Inputs.ShowMainVerseChanged(it) },
            )
        }

        is SettingsContract.Inputs.GoBack -> {
            postEvent(SettingsContract.Events.NavigateUp)
        }

        // authentication
        is SettingsContract.Inputs.AuthStateChanged -> {
            updateState { it.copy(authState = input.authState) }
        }

        is SettingsContract.Inputs.SignIn -> {
            postEvent(SettingsContract.Events.RequestNativeSignIn)
        }

        is SettingsContract.Inputs.SignOut -> {
            sideJob("sign out") {
                authRepository.signOut()
            }
        }

        // donation
        is SettingsContract.Inputs.DonationAmountChanged -> {
            updateState { it.copy(selectedDonationAmount = input.amount) }
        }

        is SettingsContract.Inputs.StartOneTimeDonation -> {
            postEventWithState {
                SettingsContract.Events.RequestNativeDonation(it.selectedDonationAmount, false)
            }
        }

        is SettingsContract.Inputs.StartRecurringDonation -> {
            postEventWithState {
                SettingsContract.Events.RequestNativeDonation(it.selectedDonationAmount, true)
            }
        }

        // reviews
        is SettingsContract.Inputs.ReviewPromptStateChanged -> {
            updateState { it.copy(showReviewPrompt = input.showReviewPrompt) }
        }

        is SettingsContract.Inputs.ReviewButtonClicked -> {
            postEvent(SettingsContract.Events.RequestNativeReview)
        }

        is SettingsContract.Inputs.ShareButtonClicked -> {
            postEvent(SettingsContract.Events.RequestNativeShare)
        }

        is SettingsContract.Inputs.ReviewCompleted -> {
            noOp()
        }

        // updates
        is SettingsContract.Inputs.AppVersionsChanged -> {
            updateState { it.copy(currentVersion = input.currentVersion, latestVersion = input.latestVersion) }
        }

        is SettingsContract.Inputs.UpdateAppButtonClicked -> {
            postEvent(SettingsContract.Events.RequestNativeAppUpdate)
        }

        is SettingsContract.Inputs.ShowMainVerseChanged -> {
            updateState { it.copy(showMainVerse = input.showMainVerse) }
        }

        is SettingsContract.Inputs.ToggleShowMainVerse -> {
            val currentState = getCurrentState()
            sideJob("ToggleShowMainVerse") {
                appPreferences.setShowMainVerse(!currentState.showMainVerse)
            }
        }
    }
}
