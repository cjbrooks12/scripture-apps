package com.caseyjbrooks.app.utils.theme

import androidx.compose.material.icons.Icons

/**
 * Composable wrapper around images used for DE from app resources. This class only includes the
 * custom icons and images that cannot be replaced by normal Compose APIs or Icons.
 *
 * Use `BrandIcons.material` to access all default Material Compose vector icons. You can search the
 * available icons at the link below, the names of icons are the same as listed in that link but in
 * camelCase.
 *
 * https://fonts.google.com/icons
 */
object BrandIcons {
    val material get() = Icons.Filled
}
