package com.caseyjbrooks.verses.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.verses.models.ArchiveStatus
import com.caseyjbrooks.ui.koin.LocalKoin
import com.copperleaf.ballast.repository.cache.getCachedOrEmptyList
import com.copperleaf.ballast.repository.cache.isLoading
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalLayoutApi::class)
public object VerseListScreen {
    @Composable
    public fun Content() {
        val koin = LocalKoin.current
        val coroutineScope = rememberCoroutineScope()
        val viewModel: VerseListViewModel = remember(coroutineScope, koin) {
            koin.get { parametersOf(coroutineScope) }
        }

        val uiState by viewModel.observeStates().collectAsState()

        Content(uiState) { viewModel.trySend(it) }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    internal fun Content(
        uiState: VerseListContract.State,
        postInput: (VerseListContract.Inputs) -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Verses") },
                actions = {
                    IconButton(onClick = { postInput(VerseListContract.Inputs.CreateNewVerse) }) {
                        Icon(Icons.Default.Add, "Create Verse")
                    }
                }
            )

            ListFilterControls(uiState, postInput)

            if (uiState.cachedVerses.isLoading()) {
                LoadingState(uiState, postInput)
            } else {
                val verseList = uiState.cachedVerses.getCachedOrEmptyList()
                if (verseList.isEmpty()) {
                    EmptyContentState(uiState, postInput)
                } else {
                    NonEmptyContentState(uiState, postInput)
                }
            }
        }
    }

    @Composable
    internal fun ColumnScope.LoadingState(
        uiState: VerseListContract.State,
        postInput: (VerseListContract.Inputs) -> Unit,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    internal fun ColumnScope.EmptyContentState(
        uiState: VerseListContract.State,
        postInput: (VerseListContract.Inputs) -> Unit,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            when (uiState.archiveStatus) {
                ArchiveStatus.FullCollection,
                ArchiveStatus.NotArchived -> {
                    Text("You have no saved verses. Click the '+' above to add a verse.")
                }

                ArchiveStatus.Archived -> {
                    Text("You have no archived verses.")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    internal fun ColumnScope.NonEmptyContentState(
        uiState: VerseListContract.State,
        postInput: (VerseListContract.Inputs) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(uiState.cachedVerses.getCachedOrEmptyList()) { verse ->
                Card(
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    onClick = { postInput(VerseListContract.Inputs.ViewVerseDetails(verse)) },
                ) {
                    Column(Modifier.padding(16.dp)) {
                        var showItemMenu by remember { mutableStateOf(false) }

                        Row(
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = verse.text.take(40),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )

                                if (verse.tags.isNotEmpty()) {
                                    Text(
                                        text = "Tags",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    FlowRow {
                                        verse.tags.forEach { tag ->
                                            FilterChip(
                                                selected = false,
                                                label = { Text(tag.tag) },
                                                onClick = {
                                                    postInput(VerseListContract.Inputs.AddTagFilter(tag))
                                                },
                                            )
                                        }
                                    }
                                }
                            }

                            Box(Modifier.size(48.dp)) {
                                IconButton({ showItemMenu = true }) {
                                    Icon(Icons.Default.MoreVert, "")
                                }

                                DropdownMenu(
                                    expanded = showItemMenu,
                                    onDismissRequest = { showItemMenu = false },
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Edit") },
                                        onClick = {
                                            showItemMenu = false
                                            postInput(VerseListContract.Inputs.EditVerse(verse))
                                        },
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Practice") },
                                        onClick = {
                                            showItemMenu = false
                                            postInput(VerseListContract.Inputs.Practice(verse))
                                        },
                                    )

                                    if (verse.archived) {
                                        DropdownMenuItem(
                                            text = { Text("Restore") },
                                            onClick = {
                                                showItemMenu = false
                                                postInput(VerseListContract.Inputs.RestoreFromArchive(verse))
                                            },
                                        )
                                    } else {
                                        DropdownMenuItem(
                                            text = { Text("Archive") },
                                            onClick = {
                                                showItemMenu = false
                                                postInput(VerseListContract.Inputs.Archive(verse))
                                            },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    internal fun ColumnScope.ListFilterControls(
        uiState: VerseListContract.State,
        postInput: (VerseListContract.Inputs) -> Unit,
    ) {
        Column(Modifier.padding(16.dp)) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ArchiveStatusFilterControl(uiState, postInput)
                TagsFilterControl(uiState, postInput)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    internal fun FlowRowScope.ArchiveStatusFilterControl(
        uiState: VerseListContract.State,
        postInput: (VerseListContract.Inputs) -> Unit,
    ) {
        var showArchiveStatusMenu by remember { mutableStateOf(false) }
        FilterChip(
            selected = false,
            onClick = { showArchiveStatusMenu = true },
            label = {
                when (uiState.archiveStatus) {
                    ArchiveStatus.NotArchived -> Text("Current")
                    ArchiveStatus.Archived -> Text("Archived")
                    ArchiveStatus.FullCollection -> Text("All")
                }
            },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Change Archive Status") }
        )

        DropdownMenu(
            expanded = showArchiveStatusMenu,
            onDismissRequest = { showArchiveStatusMenu = false },
        ) {
            when (uiState.archiveStatus) {
                ArchiveStatus.NotArchived -> {
                    DropdownMenuItem(
                        onClick = {
                            showArchiveStatusMenu = false
                            postInput(VerseListContract.Inputs.SetArchiveStatus(ArchiveStatus.FullCollection))
                        },
                        text = { Text("All") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            showArchiveStatusMenu = false
                            postInput(VerseListContract.Inputs.SetArchiveStatus(ArchiveStatus.Archived))
                        },
                        text = { Text("Archived") }
                    )
                }

                ArchiveStatus.Archived -> {
                    DropdownMenuItem(
                        onClick = {
                            showArchiveStatusMenu = false
                            postInput(VerseListContract.Inputs.SetArchiveStatus(ArchiveStatus.FullCollection))
                        },
                        text = { Text("All") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            showArchiveStatusMenu = false
                            postInput(VerseListContract.Inputs.SetArchiveStatus(ArchiveStatus.NotArchived))
                        },
                        text = { Text("Current") }
                    )
                }

                ArchiveStatus.FullCollection -> {
                    DropdownMenuItem(
                        onClick = {
                            showArchiveStatusMenu = false
                            postInput(VerseListContract.Inputs.SetArchiveStatus(ArchiveStatus.NotArchived))
                        },
                        text = { Text("Current") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            showArchiveStatusMenu = false
                            postInput(VerseListContract.Inputs.SetArchiveStatus(ArchiveStatus.Archived))
                        },
                        text = { Text("Archived") }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    internal fun FlowRowScope.TagsFilterControl(
        uiState: VerseListContract.State,
        postInput: (VerseListContract.Inputs) -> Unit,
    ) {
        if (uiState.allTags.getCachedOrEmptyList().isEmpty()) return

        uiState.tagFilter.forEach { tag ->
            FilterChip(
                selected = false,
                onClick = { postInput(VerseListContract.Inputs.RemoveTagFilter(tag)) },
                label = { Text(tag.tag) },
                trailingIcon = { Icon(Icons.Default.Cancel, "Select Tags") }
            )
        }

        if (uiState.inactiveTags.getCachedOrEmptyList().isNotEmpty()) {
            Box {
                var showTagsMenu by remember { mutableStateOf(false) }
                FilterChip(
                    selected = false,
                    onClick = { showTagsMenu = true },
                    label = { Text("Tags") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Select Tags") }
                )

                DropdownMenu(
                    expanded = showTagsMenu,
                    onDismissRequest = { showTagsMenu = false },
                ) {
                    uiState.inactiveTags.getCachedOrEmptyList().forEach { tag ->
                        DropdownMenuItem(
                            text = { Text(tag.tag) },
                            onClick = {
                                showTagsMenu = false
                                postInput(VerseListContract.Inputs.AddTagFilter(tag))
                            }
                        )
                    }
                }
            }
        }
    }
}
