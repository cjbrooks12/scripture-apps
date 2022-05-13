package com.caseyjbrooks.app.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.caseyjbrooks.app.utils.theme.BrandTheme
import com.caseyjbrooks.app.utils.theme.Spacing

@Composable
fun LabeledCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: CheckboxColors = CheckboxDefaults.colors(),
    label: @Composable () -> Unit
) {
    Row(
        modifier.clickable(
            onClick = { onCheckedChange?.invoke(!checked) },
            role = Role.Checkbox
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier
                .wrapContentSize()
                .padding(end = Spacing.medium),
            enabled = enabled,
            interactionSource = interactionSource,
            colors = colors
        )

        Box(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(vertical = Spacing.small),
            contentAlignment = Alignment.CenterStart
        ) {
            label()
        }
    }
}

@Preview
@Composable
fun PreviewLabeledCheckbox() {
    BrandTheme {
        LabeledCheckbox(true, { }) {
            Text("I accept the terms and conditions")
        }
    }
}
