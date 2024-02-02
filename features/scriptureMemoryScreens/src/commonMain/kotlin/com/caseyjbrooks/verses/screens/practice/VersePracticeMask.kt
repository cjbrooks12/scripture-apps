package com.caseyjbrooks.verses.screens.practice

import kotlin.random.Random

internal sealed interface VersePracticeMask {
    fun applyMask(
        random: Random,
        inputText: String,
        threshold: Float,
    ): String

    class NoMasking : VersePracticeMask {
        override fun applyMask(
            random: Random,
            inputText: String,
            threshold: Float,
        ): String {
            return inputText
        }
    }

    class FirstLetters : VersePracticeMask {
        override fun applyMask(
            random: Random,
            inputText: String,
            threshold: Float,
        ): String {
            return inputText
                .split("\\s+".toRegex())
                .joinToString(separator = " ") {
                    if (random.nextDouble(0.0, 1.0) >= threshold) {
                        it.take(1)
                    } else {
                        it
                    }
                }
        }
    }

    class Underscores : VersePracticeMask {
        override fun applyMask(
            random: Random,
            inputText: String,
            threshold: Float,
        ): String {
            return inputText
                .split("\\s+".toRegex())
                .joinToString(separator = " ") {
                    if (random.nextDouble(0.0, 1.0) >= threshold) {
                        String(ByteArray(it.length) { '_'.code.toByte() })
                    } else {
                        it
                    }
                }
        }
    }

    class FirstLettersWithUnderscores : VersePracticeMask {
        override fun applyMask(
            random: Random,
            inputText: String,
            threshold: Float,
        ): String {
            return inputText
                .split("\\s+".toRegex())
                .joinToString(separator = " ") {
                    if (random.nextDouble(0.0, 1.0) >= threshold) {
                        it.take(1).uppercase() + String(ByteArray(it.length - 1) { '_'.code.toByte() })
                    } else {
                        it
                    }
                }
        }
    }
}
