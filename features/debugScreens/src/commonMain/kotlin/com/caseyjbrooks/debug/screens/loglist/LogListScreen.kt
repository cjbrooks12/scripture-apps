package com.caseyjbrooks.debug.screens.loglist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.ui.koin.LocalKoin
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
public object LogListScreen {
    @Composable
    public fun Content() {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: LogListViewModel = remember(coroutineScope, koin) {
            koin.get { parametersOf(coroutineScope) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Content(
        uiState: LogListContract.State,
        postInput: (LogListContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Developer Info") },
                navigationIcon = {
                    IconButton(onClick = { postInput(LogListContract.Inputs.NavigateUp) }) {
                        Icon(Icons.Default.ArrowBack, "Navigate Up")
                    }
                },
                actions = {
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(Modifier.fillMaxWidth().wrapContentHeight()) {
                    Column(Modifier.padding(16.dp)) {

                    }
                }
            }
        }
    }
}
