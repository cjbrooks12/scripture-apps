package com.copperleaf.scripturenow.ui.settings

import com.copperleaf.ballast.InputHandler
import com.copperleaf.ballast.InputHandlerScope
import com.copperleaf.ballast.observeFlows
import com.copperleaf.ballast.postEventWithState
import com.copperleaf.scripturenow.repositories.auth.AuthRepository
import kotlinx.coroutines.flow.map

class SettingsInputHandler(
    private val authRepository: AuthRepository,
) : InputHandler<
    SettingsContract.Inputs,
    SettingsContract.Events,
    SettingsContract.State> {
    override suspend fun InputHandlerScope<
        SettingsContract.Inputs,
        SettingsContract.Events,
        SettingsContract.State>.handleInput(
        input: SettingsContract.Inputs
    ) = when (input) {
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
                    .map { SettingsContract.Inputs.AuthStateChanged(it) }
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
    }
}
