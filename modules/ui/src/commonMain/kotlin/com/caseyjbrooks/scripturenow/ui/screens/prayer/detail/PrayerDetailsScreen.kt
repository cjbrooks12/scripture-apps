package com.caseyjbrooks.scripturenow.ui.screens.prayer.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.scripturenow.ui.LocalInjector
import com.caseyjbrooks.scripturenow.ui.layouts.BottomBarLayout
import com.caseyjbrooks.scripturenow.ui.layouts.ScrollableContent
import com.caseyjbrooks.scripturenow.viewmodel.prayer.detail.PrayerDetailsContract
import com.copperleaf.ballast.repository.cache.getCachedOrThrow

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
    BottomBarLayout(
        title = { Text("Prayer Details") },
    ) {
        ScrollableContent {
            if (state.loading) {
                CircularProgressIndicator()
            } else {
                val prayer = state.prayer.getCachedOrThrow()
                Card(modifier = Modifier.fillMaxWidth().padding()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text(prayer.prayerDescription)
                        Text("Created at: ${prayer.createdAt}")
                        Text("Updated at: ${prayer.updatedAt}")
                    }
                }

                Spacer(Modifier.weight(1f))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { postInput(PrayerDetailsContract.Inputs.EditPrayer) },
                ) {
                    Text("Edit")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { postInput(PrayerDetailsContract.Inputs.DeletePrayer) },
                    colors = ButtonDefaults.outlinedButtonColors(),
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
