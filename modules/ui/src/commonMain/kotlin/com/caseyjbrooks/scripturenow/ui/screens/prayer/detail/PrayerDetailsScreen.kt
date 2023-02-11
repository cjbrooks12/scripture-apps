package com.caseyjbrooks.scripturenow.ui.screens.prayer.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.MainLayout
import com.caseyjbrooks.scripturenow.ui.layouts.ScrollableContent
import com.caseyjbrooks.scripturenow.viewmodel.prayer.detail.PrayerDetailsContract

@Composable
public fun PrayerDetailsScreen(prayerId: String) {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) {
        injector.getPrayerDetailsViewModel(
            coroutineScope,
            prayerId,
        )
    }
    val vmState by vm.observeStates().collectAsState()

    PrayerDetailsScreen(vmState) { vm.trySend(it) }
}

@Composable
public fun PrayerDetailsScreen(
    state: PrayerDetailsContract.State,
    postInput: (PrayerDetailsContract.Inputs) -> Unit,
) {
    MainLayout(
        title = { Text("Prayer Details") },
    ) {
        ScrollableContent {
            Card(modifier = Modifier.fillMaxWidth().padding()) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Welcome to Scripture Now!")
                }
            }
        }
    }
}
