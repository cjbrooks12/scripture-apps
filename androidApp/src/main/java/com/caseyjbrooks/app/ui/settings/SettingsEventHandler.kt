package com.caseyjbrooks.app.ui.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.SnackbarHostState
import com.caseyjbrooks.app.MainActivity
import com.caseyjbrooks.app.utils.await
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.scripturenow.repositories.router.MainRouterViewModel
import com.copperleaf.scripturenow.ui.settings.SettingsContract
import com.firebase.ui.auth.AuthUI
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

class SettingsEventHandler(
    private val activity: AppCompatActivity,
    private val router: MainRouterViewModel,
    private val snackbarHostState: SnackbarHostState,
) : EventHandler<
    SettingsContract.Inputs,
    SettingsContract.Events,
    SettingsContract.State> {
    override suspend fun EventHandlerScope<
        SettingsContract.Inputs,
        SettingsContract.Events,
        SettingsContract.State>.handleEvent(
        event: SettingsContract.Events
    ) = when (event) {
        is SettingsContract.Events.NavigateUp -> {
            router.send(RouterContract.Inputs.GoBack)
        }
        is SettingsContract.Events.RequestNativeSignIn -> {
            val mainActivity = activity as MainActivity

            mainActivity
                .signInLauncher
                .launch(
                    AuthUI
                        .getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                            listOf(
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.PhoneBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.AnonymousBuilder().build(),
                            )
                        )
                        .build()
                )

            val signInResult = mainActivity.firebaseSignInResultChannel.receive()

            val response = signInResult.idpResponse
            if (signInResult.resultCode == RESULT_OK) {
                // Successfully signed in
                snackbarHostState.showSnackbar("Sign-in successful")
            } else {
                if (response == null) {
                    snackbarHostState.showSnackbar("Sign-in cancelled")
                } else {
                    response.error?.let { Firebase.crashlytics.recordException(it) }
                    snackbarHostState.showSnackbar("Unknown error, try again later")
                }
            }

            Unit
        }
        is SettingsContract.Events.RequestNativeDonation -> {

        }
        is SettingsContract.Events.RequestNativeReview -> {
            val manager = ReviewManagerFactory.create(activity)

            manager
                .requestReviewFlow()
                .await()
                .getOrNull()
                ?.let { reviewInfo ->
                    manager.launchReviewFlow(activity, reviewInfo).addOnCompleteListener {
                        // do nothing
                    }
                }

            Unit
        }
        is SettingsContract.Events.RequestNativeShare -> {
            Intent()
                .apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Download this app to memorize Bible verses with me! https://play.google.com/store/apps/details?id=com.caseybrooks.scripturememory")
                    type = "text/plain"
                }
                .let { Intent.createChooser(it, null) }
                .let { activity.startActivity(it) }
        }
        is SettingsContract.Events.RequestNativeAppUpdate -> {

        }
    }
}
