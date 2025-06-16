package com.copperleaf.abide.minigames.verses.typeverse

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import kotlin.random.Random

fun getWords(text: String): List<String> {
    return text.split(" ")
}

fun reformatText(
    text: String,
    reformatWord: (String) -> String,
    random: Random,
    threshold: Float,
    progress: Int,
): AnnotatedString {
    return buildAnnotatedString {
        val words = getWords(text)
        words.forEachIndexed { index, word ->
            val isCurrentWord = index == progress

            if (isCurrentWord) {
                pushStyle(SpanStyle(color = Color.Red))
            }

            if (index < progress) {
                append(word)
            } else if (random.nextFloat() > threshold) {
                append(word)
            } else {
                append(reformatWord(word))
            }
            append(' ')

            if (isCurrentWord) {
                pop()
            }
        }
    }


}
