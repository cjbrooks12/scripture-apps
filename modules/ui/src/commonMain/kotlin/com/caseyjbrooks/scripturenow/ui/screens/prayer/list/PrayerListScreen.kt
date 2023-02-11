package com.caseyjbrooks.scripturenow.ui.screens.prayer.list

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
import com.caseyjbrooks.scripturenow.viewmodel.prayer.list.PrayerListContract

@Composable
public fun PrayerListScreen() {
    val injector = LocalInjector.current
    val coroutineScope = rememberCoroutineScope()
    val vm = remember(injector, coroutineScope) { injector.getPrayerListViewModel(coroutineScope) }
    val vmState by vm.observeStates().collectAsState()

    PrayerListScreen(vmState) { vm.trySend(it) }
}

@Composable
public fun PrayerListScreen(
    state: PrayerListContract.State,
    postInput: (PrayerListContract.Inputs) -> Unit,
) {
    MainLayout(
        title = { Text("Prayer") },
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
