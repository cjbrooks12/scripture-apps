package com.caseyjbrooks.app.ui.settings

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.scripturenow.ui.Destinations

class SettingsScreen : ComposeScreen(Destinations.App.Settings) {

    @Composable
    override fun ScreenContent(destination: Destination) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Settings") },
                )
            },
            content = { contentPadding ->

            }
        )
    }
}
