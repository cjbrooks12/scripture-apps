package com.caseyjbrooks.app.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caseyjbrooks.app.utils.theme.localButtonColors
import com.caseyjbrooks.app.utils.thenIfNotNull
import kotlinx.coroutines.delay

@Composable
fun ProgressButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClickWhenDisabled: (() -> Unit)? = null,
    loading: Boolean = false,
    enabled: Boolean = true,
    label: @Composable () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier,
        colors = ButtonDefaults.localButtonColors(),
    ) {
        Row(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .thenIfNotNull(onClickWhenDisabled, enabled = !enabled) {
                    Modifier.clickable { it() }
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (loading) {
                CircularProgressIndicator()
            } else {
                label()
            }
        }
    }
}

/**
 * Mirrors semantics of normal Androidx [androidx.core.widget.ContentLoadingProgressBar], but for
 * arbitrary content. To avoid annoying "flashes" of content for rapidly-loading items, this loader
 * waits a minimum of [delayMillis] milliseconds before displaying the actual [content].
 */
@Composable
fun ContentLoading(
    delayMillis: Long = 500L,
    content: @Composable () -> Unit
) {
    val isVisible: Boolean by produceState(false) {
        delay(delayMillis)
        value = true
    }

    if (isVisible) {
        content()
    }
}

/**
 * Mirrors semantics of normal Androidx [androidx.core.widget.ContentLoadingProgressBar]. To avoid
 * annoying "flashes" of content for rapidly-loading items, this loader waits a minimum of
 * [delayMillis] milliseconds before displaying the actual [progressLoader].
 */
@Composable
fun ContentLoadingProgressIndicator(
    delayMillis: Long = 500L,
    progressLoader: @Composable () -> Unit = { CircularProgressIndicator() }
) {
    ContentLoading(delayMillis) {
        progressLoader()
    }
}

/**
 * A [Box] for content that may take time to load. When the content is still loading ([isLoading] is
 * true), a [ContentLoadingProgressIndicator] will be displayed in the center of the box area, but
 * only after a delay of [delayMillis] to avoid annoying flashes of content. Once the content has
 * finished loading ([isLoading] becomes false), this will behave like a normal [Box].
 */
@Composable
fun ContentLoadingLayout(
    isLoading: Boolean,
    modifier: Modifier = Modifier.fillMaxSize(),

    // properties for the progress indicator
    delayMillis: Long = 500L,
    progressLoader: @Composable () -> Unit = { CircularProgressIndicator() },

    // properties for the content box when it is loaded
    content: @Composable () -> Unit
) {
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier,
        ) {
            ContentLoadingProgressIndicator(delayMillis, progressLoader)
        }
    } else {
        content()
    }
}
