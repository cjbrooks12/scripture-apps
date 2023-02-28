package com.caseyjbrooks.scripturenow.viewmodel.settings

import com.caseyjbrooks.scripturenow.models.votd.VerseOfTheDayService
import com.caseyjbrooks.scripturenow.repositories.global.GlobalRepositoryContract

public object SettingsContract {
    public data class State(
// global state
        val globalState: GlobalRepositoryContract.State = GlobalRepositoryContract.State(),

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

    public sealed class Inputs {
        public object Initialize : Inputs()
        public object GoBack : Inputs()

        // authentication
        public data class GlobalStateChanged(val globalState: GlobalRepositoryContract.State) : Inputs()
        public object SignIn : Inputs()
        public object SignOut : Inputs()

        // donation
        public data class DonationAmountChanged(val amount: Int) : Inputs()
        public object StartOneTimeDonation : Inputs()
        public object StartRecurringDonation : Inputs()

        // reviews
        public data class ReviewPromptStateChanged(val showReviewPrompt: Boolean) : Inputs()
        public object ReviewButtonClicked : Inputs()
        public object ShareButtonClicked : Inputs()
        public object ReviewCompleted : Inputs()

        // updates
        public data class AppVersionsChanged(val currentVersion: String, val latestVersion: String) : Inputs()
        public object UpdateAppButtonClicked : Inputs()

        // preferences
        public object ToggleShowMainVerse : Inputs()
        public data class SetVerseOfTheDayServicePreference(val verseOfTheDayService: VerseOfTheDayService) : Inputs()
    }

    public sealed class Events {
        public object NavigateUp : Events()

        public object RequestNativeSignIn : Events()
        public data class RequestNativeDonation(val amount: Int, val recurring: Boolean) : Events()
        public object RequestNativeReview : Events()
        public object RequestNativeShare : Events()
        public object RequestNativeAppUpdate : Events()
    }
}
