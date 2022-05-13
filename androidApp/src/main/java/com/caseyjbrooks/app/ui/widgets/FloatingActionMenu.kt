package com.caseyjbrooks.app.ui.widgets

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caseyjbrooks.app.utils.theme.BrandIcons

// This file was copied from https://github.com/ComposeAcademy/ComposeCompanion, which has no OSS
// license. That repo was based on an old version of Compose and was not compatible with the app. It
// has been heavily updated to use the current Compose APIs and standard components rather than
// drawing everything to Canvas manually.

class MultiFabItem(
    val label: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val onClicked: () -> Unit,
)

enum class MultiFabState {
    COLLAPSED, EXPANDED
}

@Composable
fun MultiFloatingActionButton(
    fabIcon: @Composable (rotation: Float) -> Unit = { rotation ->
        Icon(
            imageVector = BrandIcons.material.Add,
            contentDescription = "Add",
            modifier = Modifier.rotate(rotation)
        )
    },
    items: List<MultiFabItem>,
    showLabels: Boolean = true,
) {
    var state by remember { mutableStateOf(MultiFabState.COLLAPSED) }

    MultiFloatingActionButton(
        fabIcon = fabIcon,
        items = items,
        toState = state,
        showLabels = showLabels,
        stateChanged = { state = it },
    )
}

@Composable
fun MultiFloatingActionButton(
    fabIcon: @Composable (rotation: Float) -> Unit,
    items: List<MultiFabItem>,
    toState: MultiFabState,
    showLabels: Boolean = true,
    stateChanged: (fabstate: MultiFabState) -> Unit,
) {
    val transition: Transition<MultiFabState> = updateTransition(
        targetState = toState,
        label = "fabMenu"
    )
    val scale: Float by transition.animateFloat(
        label = "scale"
    ) { state ->
        if (state == MultiFabState.EXPANDED) 1f else 0f
    }
    val alpha: Float by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 50) },
        label = "alpha"
    ) { state ->
        if (state == MultiFabState.EXPANDED) 1f else 0f
    }
    val shadow: Dp by transition.animateDp(
        transitionSpec = { tween(durationMillis = 50) },
        label = "shadow"
    ) { state ->
        if (state == MultiFabState.EXPANDED) 2.dp else 0.dp
    }
    val rotation: Float by transition.animateFloat(
        label = "rotation"
    ) { state ->
        if (state == MultiFabState.EXPANDED) 45f else 0f
    }

    Column(horizontalAlignment = Alignment.End) {
        items.forEach { item ->
            MiniFabItem(item, alpha, shadow, scale, showLabels) {
                item.onClicked()
                stateChanged(MultiFabState.COLLAPSED)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        FloatingActionButton(
            onClick = {
                stateChanged(
                    if (transition.currentState == MultiFabState.EXPANDED) {
                        MultiFabState.COLLAPSED
                    } else {
                        MultiFabState.EXPANDED
                    }
                )
            }
        ) {
            fabIcon(rotation)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MiniFabItem(
    item: MultiFabItem,
    alpha: Float,
    shadow: Dp,
    scale: Float,
    showLabel: Boolean,
    onClick: ()->Unit,
) {
    val fabColor = MaterialTheme.colors.secondary
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        if (showLabel) {
            Card(
                modifier = Modifier.alpha(animateFloatAsState(alpha).value),
                elevation = animateDpAsState(shadow).value,
            ) {
                Box(Modifier.padding(horizontal = 6.dp, vertical = 4.dp)) {
                    ProvideTextStyle(
                        value = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    ) {
                        item.label()
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Card(
            onClick = { onClick() },
            modifier = Modifier
                .alpha(animateFloatAsState(alpha).value)
                .size(40.dp)
                .scale(animateFloatAsState(scale).value),
            elevation = animateDpAsState(shadow).value,
            shape = CircleShape,
            backgroundColor = fabColor,
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
                    CompositionLocalProvider(LocalContentAlpha provides alpha) {
                        item.icon()
                    }
                }
            }
        }
    }
}
