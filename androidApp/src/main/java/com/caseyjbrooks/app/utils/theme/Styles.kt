package com.caseyjbrooks.app.utils.theme

import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable

val onPrimarySurface: Boolean
    @Composable get() {
        return LocalContentColor.current == MaterialTheme.colors.onPrimary
    }

/**
 * On normal surface, use normal button colors. On a primary surface, use a white button with
 * primary content color.
 */
@Composable
fun ButtonDefaults.localButtonColors(): ButtonColors {
    return if (onPrimarySurface) {
        buttonColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.primary,
        )
    } else {
        buttonColors()
    }
}

/**
 * On normal surface, use normal button colors. On a primary surface, use a white button with
 * primary content color.
 */
@Composable
fun ButtonDefaults.localTextButtonColors(): ButtonColors {
    return if(onPrimarySurface) {
        textButtonColors(
            contentColor = MaterialTheme.colors.onPrimary
        )
    } else {
        textButtonColors()
    }
}

/**
 * On normal surface, use normal text field colors. On a primary surface, use white for anything
 * that's typically primary-colored.
 */
@Composable
fun TextFieldDefaults.localTextFieldColors(): TextFieldColors {
    if (onPrimarySurface) {
        val textColor = MaterialTheme.colors.onPrimary
        val textFieldColoredComponentsColor = MaterialTheme.colors.onPrimary
        return textFieldColors(
            textColor = textColor,
            cursorColor = textFieldColoredComponentsColor,
            focusedIndicatorColor = textFieldColoredComponentsColor.copy(alpha = ContentAlpha.high),
            leadingIconColor = textFieldColoredComponentsColor.copy(alpha = IconOpacity),
            disabledLeadingIconColor = textFieldColoredComponentsColor.copy(alpha = ContentAlpha.disabled),
            errorLeadingIconColor = textFieldColoredComponentsColor,
            trailingIconColor = textFieldColoredComponentsColor.copy(alpha = IconOpacity),
            disabledTrailingIconColor = textFieldColoredComponentsColor.copy(alpha = ContentAlpha.disabled),
            focusedLabelColor = textFieldColoredComponentsColor.copy(alpha = ContentAlpha.high),
            unfocusedLabelColor = textFieldColoredComponentsColor.copy(ContentAlpha.medium),
            placeholderColor = textFieldColoredComponentsColor.copy(ContentAlpha.medium),
            disabledPlaceholderColor = textFieldColoredComponentsColor.copy(ContentAlpha.disabled)
        )
    } else {
        return TextFieldDefaults.textFieldColors()
    }
}
