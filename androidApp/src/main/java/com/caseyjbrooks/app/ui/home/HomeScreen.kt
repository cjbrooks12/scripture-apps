package com.caseyjbrooks.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.app.utils.ComposeScreen
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.scripturenow.ui.Destinations
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

class HomeScreen : ComposeScreen(Destinations.App.Home) {

    @Composable
    override fun ScreenContent(destination: Destination) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Scripture Now") },
                )
            },
            content = { contentPadding ->
                SwipeRefresh(
                    state = rememberSwipeRefreshState(false),
                    onRefresh = { },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(
                                top = contentPadding.calculateTopPadding() + 16.dp,
                                bottom = contentPadding.calculateBottomPadding() + 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                            ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth().padding()) {
                            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                                Text("Welcome to Scripture Now!")

                            }
                        }
                    }
                }
            }
        )
    }
}
