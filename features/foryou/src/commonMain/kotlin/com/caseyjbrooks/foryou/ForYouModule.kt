package com.caseyjbrooks.foryou

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import com.caseyjbrooks.di.FeatureModule
import com.caseyjbrooks.foryou.ui.foryou.ForYouRoute
import com.caseyjbrooks.routing.NavigationItem
import com.caseyjbrooks.routing.ScriptureNowScreen

public class ForYouModule : FeatureModule {
    override val routes: List<ScriptureNowScreen> = listOf(
        ForYouRoute,
    )
    override val initialRoute: ScriptureNowScreen = ForYouRoute
    override val mainNavigationItems: List<NavigationItem> = listOf(
        NavigationItem(
            route = ForYouRoute,
            iconFilled = Icons.Filled.Face,
            iconOutlined = Icons.Outlined.Face,
            label = "For You",
            order = 10,
        ),
    )
    override val secondaryNavigationItems: List<NavigationItem> = listOf()
}
