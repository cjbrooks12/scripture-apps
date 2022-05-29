package com.copperleaf.scripturenow.verses.mtg

import kotlinx.serialization.Serializable

enum class MtgColor {
    Black, Green, White, Red, Blue, Colorless,
}

@Serializable
data class MtgColorCost(val count: Int, val color: MtgColor)
