package com.caseyjbrooks.app.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.caseyjbrooks.R
import com.caseyjbrooks.app.utils.theme.BrandIcons

enum class BrandScaffoldHeaderIconType {
    Back, Close,
}

// TODO: integrate SwipeRefresh directly into this
@Composable
fun BrandScaffold(
    @StringRes title: Int,
    snackbarHostState: SnackbarHostState,
    headerIconType: BrandScaffoldHeaderIconType = BrandScaffoldHeaderIconType.Close,
    onHeaderIconClick: ()->Unit,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable ()->Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(title)) },
                navigationIcon = {
                    IconButton(onClick = onHeaderIconClick) {
                        when(headerIconType) {
                            BrandScaffoldHeaderIconType.Back -> {
                                Icon(BrandIcons.material.ArrowBack, stringResource(R.string.back))
                            }
                            BrandScaffoldHeaderIconType.Close -> {
                                Icon(BrandIcons.material.Close, stringResource(R.string.close))
                            }
                        }
                    }
                }
            )
        },
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        floatingActionButton = floatingActionButton,
        content = { content() }
    )
}
