package com.caseyjbrooks.app.ui.verses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.app.ui.widgets.EditVerseForm
import com.caseyjbrooks.app.utils.ComposeScreen
import com.caseyjbrooks.app.utils.theme.LocalInjector
import com.copperleaf.ballast.navigation.routing.Destination
import com.copperleaf.scripturenow.ui.Destinations
import com.copperleaf.scripturenow.ui.verses.edit.CreateOrEditMemoryVerseContract

class NewVerseScreen : ComposeScreen(Destinations.App.Verses.Create) {

    @Composable
    override fun ScreenContent(destination: Destination) {
        val coroutineScope = rememberCoroutineScope()
        val injector = LocalInjector.current
        val vm = remember(coroutineScope, injector) { injector.createOrEditVerseViewModel(coroutineScope) }
        val vmState by vm.observeStates().collectAsState()

        LaunchedEffect(vm) {
            vm.trySend(CreateOrEditMemoryVerseContract.Inputs.Initialize(null))
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { vm.trySend(CreateOrEditMemoryVerseContract.Inputs.GoBack) }) {
                            Icon(Icons.Default.ArrowBack, "Go Back")
                        }
                    },
                    title = { Text("Add Memory Verse") },
                    actions = {
                        IconButton(onClick = { vm.trySend(CreateOrEditMemoryVerseContract.Inputs.SaveVerse) }) {
                            Icon(Icons.Default.Save, "Save verse")
                        }
                    }
                )
            },
            content = { contentPadding ->
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
                ) {
                    if(vmState.editingVerse != null) {
                        EditVerseForm(
                            vmState.editingVerse!!
                        ) {
                            vm.trySend(CreateOrEditMemoryVerseContract.Inputs.UpdateVerse(it))
                        }
                    }
                }
            },
        )
    }
}
