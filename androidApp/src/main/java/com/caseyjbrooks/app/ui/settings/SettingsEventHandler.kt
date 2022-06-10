package com.caseyjbrooks.app.ui.settings

import android.app.Activity.RESULT_OK
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.SnackbarHostState
import com.caseyjbrooks.app.MainActivity
import com.copperleaf.ballast.EventHandler
import com.copperleaf.ballast.EventHandlerScope
import com.copperleaf.ballast.navigation.routing.RouterContract
import com.copperleaf.scripturenow.repositories.router.MainRouterViewModel
import com.copperleaf.scripturenow.ui.settings.SettingsContract
import com.firebase.ui.auth.AuthUI

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
        is SettingsContract.Events.RequestSignIn -> {
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
                    snackbarHostState.showSnackbar(response.error?.message ?: "Unknown error, try again later")
                }
            }

            Unit
        }
    }
}
