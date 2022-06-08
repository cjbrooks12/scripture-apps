package com.caseyjbrooks.app.ui.votd

import androidx.compose.foundation.layout.Column
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontStyle
import com.caseyjbrooks.app.utils.ComposeScreen
import com.caseyjbrooks.app.utils.theme.LocalInjector
import com.copperleaf.ballast.repository.cache.getCachedOrNull
import com.copperleaf.ballast.repository.cache.isLoading
import com.copperleaf.ballast.router.routing.Route
import com.copperleaf.scripturenow.ui.Destinations

class VotdScreen : ComposeScreen() {
    override val screenName: String = "VotdScreen"

    override val route: Route = Destinations.App.VerseOfTheDay

    @Composable
    override fun ScreenContent() {
        Column {
            val coroutineScope = rememberCoroutineScope()
            val injector = LocalInjector.current
            val vm = remember(coroutineScope, injector) { injector.votdViewModel(coroutineScope) }
            val vmState by vm.observeStates().collectAsState()

            Text("Verse of the Day")

            if (vmState.verseOfTheDay.isLoading()) {
                CircularProgressIndicator()
            } else {
                vmState.verseOfTheDay.getCachedOrNull()?.let { votd ->
                    Text(votd.text)
                    Text(votd.reference, fontStyle = FontStyle.Italic)
                }
            }
        }
    }
}
