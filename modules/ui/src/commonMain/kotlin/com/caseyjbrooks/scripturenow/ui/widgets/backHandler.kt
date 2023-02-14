package com.caseyjbrooks.scripturenow.ui.widgets

import androidx.compose.runtime.Composable

@Composable
public expect fun backHandler(enabled: Boolean = true, onBackPressed: ()->Unit)
