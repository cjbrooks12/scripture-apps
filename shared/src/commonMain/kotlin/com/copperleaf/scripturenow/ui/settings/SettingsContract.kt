package com.copperleaf.scripturenow.ui.settings

import com.copperleaf.scripturenow.repositories.auth.models.AuthState

object SettingsContract {
    data class State(
        // authentication
        val authState: AuthState = AuthState.SignedOut,

        // donation
        val donationAmounts: List<Int> = listOf(1, 5, 10, 25),
        val selectedDonationAmount: Int = 1,

        // reviews
        val showReviewPrompt: Boolean = false,

        // updates
        val updateIsAvailable: Boolean = false,
        val currentVersion: String = "",
        val latestVersion: String = "",
    )

    sealed class Inputs {
        data class Initialize(val forceRefresh: Boolean) : Inputs()
        object GoBack : Inputs()

        // authentication
        data class AuthStateChanged(val authState: AuthState) : Inputs()
        object SignIn : Inputs()
        object SignOut : Inputs()

        // donation
        data class DonationAmountChanged(val amount: Int) : Inputs()
        object StartOneTimeDonation : Inputs()
        object StartRecurringDonation : Inputs()

        // reviews
        data class ReviewPromptStateChanged(val showReviewPrompt: Boolean) : Inputs()
        object ReviewButtonClicked : Inputs()
        object ShareButtonClicked : Inputs()
        object ReviewCompleted : Inputs()

        // updates
        data class AppVersionsChanged(val currentVersion: String, val latestVersion: String, ) : Inputs()
        data class UpdateAppButtonClicked(val currentVersion: String, val latestVersion: String, ) : Inputs()
    }

    sealed class Events {
        object NavigateUp : Events()

        object RequestNativeSignIn : Events()
        data class RequestNativeDonation(val amount: Int, val recurring: Boolean) : Events()
        object RequestNativeReview : Events()
        object RequestNativeShare : Events()
        object RequestNativeAppUpdate : Events()
    }
}
