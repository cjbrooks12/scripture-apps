package com.caseyjbrooks.app.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.scripturenow.ui.Destinations

class HomeScreen : ComposeScreen(Destinations.App.Home) {

    @Composable
    override fun screenContent(destination: Destination): Content {
        return rememberHomescreenContent(
            appBarContent = {
                TopAppBar(
                    title = { Text("Scripture Now") },
                )
            },
            mainContent = {
                Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth().padding()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text("Welcome to Scripture Now!")

                    }
                }
            }
        )
    }
}
