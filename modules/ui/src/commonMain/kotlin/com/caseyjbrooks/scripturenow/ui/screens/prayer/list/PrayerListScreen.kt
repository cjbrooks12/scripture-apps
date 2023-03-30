package com.caseyjbrooks.scripturenow.ui.screens.prayer.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.BottomBarLayout
import com.caseyjbrooks.scripturenow.ui.layouts.LazyContent
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
    BottomBarLayout(
        title = { Text("Prayer") },
    ) {
        LazyContent(
            state.prayers,
            onItemClick = { postInput(PrayerListContract.Inputs.ViewPrayer(it)) },
            beforeItems = {
                stickyHeader {
                    Button(
                        onClick = { postInput(PrayerListContract.Inputs.CreatePrayer) },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Add Prayer") }
                }
            },
        ) { prayer ->
            Text(prayer.prayerDescription)
        }
    }
}
