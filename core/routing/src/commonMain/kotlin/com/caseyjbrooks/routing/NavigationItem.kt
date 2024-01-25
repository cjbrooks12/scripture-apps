package com.caseyjbrooks.routing

import androidx.compose.ui.graphics.vector.ImageVector

public data class NavigationItem(
    val route: ScriptureNowScreen,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    val label: String,
    val order: Int,
)
